import { check, sleep } from 'k6';
import http from 'k6/http';
import exec from 'k6/execution';
import { Rate, Trend } from 'k6/metrics';

const BASE_URL = 'http://127.0.0.1:8080';
const profile = __ENV.PROFILE || 'smoke';
const profiles = {
  smoke: { executor: 'shared-iterations', vus: 1, iterations: 5, maxDuration: '10s' },
  load: { executor: 'constant-vus', vus: 5, duration: '12s' },
  arrival: {
    executor: 'constant-arrival-rate',
    rate: 24,
    timeUnit: '1s',
    duration: '12s',
    preAllocatedVUs: 12,
    maxVUs: 20,
  },
};

if (!Object.hasOwn(profiles, profile)) {
  throw new Error('PROFILE must be smoke, load, or arrival');
}

const journeySuccess = new Rate('journey_success');
const searchQueueTime = new Trend('search_queue_ms', true);
const terms = ['laptop', 'headphones', 'keyboard'];

export const options = {
  scenarios: { shop: profiles[profile] },
  summaryTrendStats: ['avg', 'med', 'p(95)', 'p(99)', 'max'],
  thresholds: {
    http_req_failed: ['rate<0.01'],
    'http_req_duration{name:search}': ['p(95)<350'],
    'http_req_duration{name:details}': ['p(95)<250'],
    journey_success: ['rate==1'],
    ...(profile === 'arrival' ? { dropped_iterations: ['count==0'] } : {}),
  },
};

function jsonOrNull(response) {
  try {
    return response.json();
  } catch {
    return null;
  }
}

export default function () {
  const term = terms[exec.scenario.iterationInTest % terms.length];
  const search = http.get(BASE_URL + '/api/products?q=' + encodeURIComponent(term), {
    tags: { name: 'search' },
    timeout: '2s',
    redirects: 0,
  });
  const products = jsonOrNull(search)?.products;
  const productId = Array.isArray(products) ? products[0]?.id : undefined;
  const queueHeader = search.headers['X-Queue-Wait-Ms'];
  if (queueHeader !== undefined && Number.isFinite(Number(queueHeader))) {
    searchQueueTime.add(Number(queueHeader));
  }

  const searchOk = check(search, {
    'search returns HTTP 200': (res) => res.status === 200,
    'search returns a product ID': () => typeof productId === 'string' && productId.length > 0,
  });

  let detailsOk = false;
  if (searchOk) {
    const details = http.get(BASE_URL + '/api/products/' + encodeURIComponent(productId), {
      tags: { name: 'details' },
      timeout: '2s',
      redirects: 0,
    });
    detailsOk = check(details, {
      'details returns HTTP 200': (res) => res.status === 200,
      'details returns the selected product': (res) => jsonOrNull(res)?.id === productId,
    });
  }

  journeySuccess.add(searchOk && detailsOk);
  if (profile === 'load') sleep(0.5);
}
