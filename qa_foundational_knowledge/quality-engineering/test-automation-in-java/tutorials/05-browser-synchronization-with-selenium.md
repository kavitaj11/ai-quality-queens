# 05. Browser synchronization with Selenium

**Intermediate** · [Guide and prerequisites](../README.md) · [Worked example](../examples/selenium/ShippingBrowser.java)

WebDriver sends a click before the asynchronous quote finishes. The local page briefly displays Calculating… . An element-presence check would succeed even before the quote arrives. The explicit wait repeatedly reads the status text and succeeds only when the qualifying subtotal has produced Free shipping.

## Run the example

From the guide directory, after completing the prerequisites in the guide:

```sh
jbang examples/selenium/ShippingBrowser.java
```

## Interview question

**Why avoid Thread.sleep after clicking?**

A fixed delay wastes time on fast runs and can expire too soon on slow ones. Wait for the observable completion condition. Bound the wait, keep the selector specific, and always quit the driver so a failed check releases its browser process.

## Practice

With the demo running, execute the example. Change only the subtotal to 4999. The wait should time out because the page now correctly shows a paid quote. Inspect that text before changing any timeout.
