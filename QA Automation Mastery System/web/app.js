'use strict';
const main = document.querySelector('main');
const esc = value => String(value).replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const storageKey = 'ai-quality-queens.mastery.v1';
let data, state = { lessons: {}, weeks: {} };
try {
  const saved = JSON.parse(localStorage.getItem(storageKey));
  if (saved?.lessons && saved?.weeks) state = saved;
} catch { /* Progress is optional when storage is unavailable. */ }
function save() {
  try { localStorage.setItem(storageKey, JSON.stringify(state)); }
  catch { document.getElementById('storage-warning')?.remove(); main.insertAdjacentHTML('afterbegin','<p id="storage-warning" role="status">Progress is available for this session only.</p>'); }
}
const key = (m,t) => m.id + '/' + t.id;
const route = (m,t) => '#/module/' + m.id + (t ? '/' + t.id : '');
const allTopics = () => data.modules.flatMap(m => m.topics.map(t => ({m,t})));
const done = (m,t) => Boolean(state.lessons[key(m,t)]);
const lessonLink = (m,t) => '<a class="item" href="'+route(m,t)+'">'+esc(t.title)+(done(m,t)?' · Completed':'')+'</a>';
const back = '<a class="back" href="#/">← Curriculum</a>';
function dashboard() {
  const topics = allTopics(), count = topics.filter(({m,t})=>done(m,t)).length;
  const next = topics.find(({m,t})=>!done(m,t)) || topics[0];
  main.innerHTML = '<section class="hero"><span class="queen" aria-hidden="true">♛</span><h1>QA Automation Mastery</h1><p>'+data.weeks.length+' weeks · '+data.modules.length+' modules · '+topics.length+' lessons</p><a class="button" href="'+route(next.m,next.t)+'">'+(count===topics.length?'Review curriculum':count?'Continue learning':'Start learning')+' →</a></section><div class="progress"><strong>Your progress</strong><progress aria-label="Lessons completed" value="'+count+'" max="'+topics.length+'"></progress><span>'+count+' of '+topics.length+' lessons</span></div><div class="row"><h2>Learning modules</h2><input aria-label="Search modules and topics" type="search" placeholder="Search modules and topics"></div><div id="results"></div>';
  const results = document.querySelector('#results');
  function search(query) {
    const q = query.trim().toLowerCase();
    if (q) {
      const hits = topics.filter(({m,t})=>[m.title,t.title,t.category].some(x=>x.toLowerCase().includes(q)));
      results.innerHTML = '<p role="status">'+hits.length+' matching lessons</p>'+hits.map(({m,t})=>'<a class="item" href="'+route(m,t)+'"><small>'+esc(m.title)+'</small><br>'+esc(t.title)+'</a>').join('');
      return;
    }
    results.innerHTML='<div class="grid">'+data.modules.map(m=>'<a class="card" href="'+route(m)+'"><h3>'+esc(m.title)+'</h3><p>'+esc(m.description)+'</p><span class="meta">'+m.topics.filter(t=>done(m,t)).length+' / '+m.topics.length+' lessons →</span></a>').join('')+'</div>';
  }
  document.querySelector('[type=search]').oninput=e=>search(e.target.value);
  search('');
}
function modulePage(m) {
  const categories = [...new Set(m.topics.map(t=>t.category))];
  main.innerHTML=back+'<h1>'+esc(m.title)+'</h1><p>'+esc(m.description)+'</p><section class="panel"><h2>Practice assignment</h2>'+esc(m.practice)+'</section>'+categories.map(c=>'<h2>'+esc(c)+'</h2>'+m.topics.filter(t=>t.category===c).map(t=>lessonLink(m,t)).join('')).join('');
}
function lesson(m,t) {
  const topics=allTopics(), i=topics.findIndex(x=>x.t===t);
  function adjacent(index,label) {
    const entry=topics[index];
    return entry?'<a href="'+route(entry.m,entry.t)+'">'+label+'</a>':'';
  }
  main.innerHTML='<a class="back" href="'+route(m)+'">← '+esc(m.title)+'</a><article class="panel"><span class="meta">'+esc(t.category)+'</span><h1>'+esc(t.title)+'</h1>'+t.body+'</article><div class="actions"><button id="complete"></button>'+adjacent(i-1,'← Previous lesson')+adjacent(i+1,'Next lesson →')+'</div>';
  const button=document.querySelector('#complete');
  function update(){button.textContent=done(m,t)?'✓ Completed':'Mark complete';button.setAttribute('aria-pressed',done(m,t));}
  button.onclick=()=>{state.lessons[key(m,t)]=!done(m,t);save();update();};update();
}
function planner() {
  main.innerHTML=back+'<h1>Weekly planner</h1><p>Check off a week when you have a test result, trace, or design note to show for it.</p>'+data.weeks.map(([n,s])=>'<label class="item"><input type="checkbox" value="'+n+'" '+(state.weeks[n]?'checked':'')+'><strong>Week '+n+'</strong> · '+esc(s)+'</label>').join('');
  main.querySelectorAll('input').forEach(input=>input.onchange=()=>{state.weeks[input.value]=input.checked;save();});
}
function flashcards(i) {
  const c=data.cards[i];
  main.innerHTML=back+'<h1>Flashcards</h1><article class="panel"><span class="meta">'+esc(c.category)+' · '+(i+1)+' of '+data.cards.length+'</span><h2>'+esc(c.question)+'</h2><button id="reveal" aria-expanded="false">Show answer</button><p id="answer" hidden>'+esc(c.answer)+'</p></article><div class="actions"><a class="button secondary" href="#/flashcards/'+((i+data.cards.length-1)%data.cards.length)+'">← Previous</a><a class="button" href="#/flashcards/'+((i+1)%data.cards.length)+'">Next card →</a></div>';
  const button=document.querySelector('#reveal'),answer=document.querySelector('#answer');
  button.onclick=()=>{answer.hidden=!answer.hidden;button.setAttribute('aria-expanded',!answer.hidden);button.textContent=answer.hidden?'Show answer':'Hide answer';};
}
function render() {
  const [page,id,topic]=location.hash.replace(/^#\/?/,'').split('/');
  document.querySelectorAll('nav a').forEach(a=>{if(a.hash===('#/'+(['planner','flashcards'].includes(page)?page:'')))a.setAttribute('aria-current','page');else a.removeAttribute('aria-current');});
  const m=data.modules.find(x=>x.id===id),t=m?.topics.find(x=>x.id===topic);
  if(!page)dashboard();
  else if(page==='planner')planner();
  else if(page==='flashcards' && (id===undefined || (/^\d+$/.test(id)&&Number(id)<data.cards.length)))flashcards(Number(id||0));
  else if(page==='module' && m && !topic)modulePage(m);
  else if(page==='module' && t)lesson(m,t);
  else main.innerHTML=back+'<h1>Page not found</h1>';
  document.title=(main.querySelector('h1')?.textContent || 'Curriculum')+' · AI Quality Queens';
  window.scrollTo(0,0);
}
document.querySelector('.skip').onclick=e=>{e.preventDefault();main.focus();};
fetch('./curriculum.json').then(r=>{if(!r.ok)throw Error();return r.json();}).then(content=>{data=content;render();window.addEventListener('hashchange',()=>{render();main.focus({preventScroll:true});});}).catch(()=>{main.textContent='The curriculum could not load. Refresh the page to try again.';});
