function base64urlToUint8array(base64Bytes) {
    const padding = '===='.substring(0, (4 - (base64Bytes.length % 4)) % 4);
    return base64js.toByteArray((base64Bytes + padding).replace(/\//g, "_").replace(/\+/g, "-"));
}
function uint8arrayToBase64url(bytes) {
    if (bytes instanceof Uint8Array) {
        return base64js.fromByteArray(bytes).replace(/\+/g, "-").replace(/\//g, "_").replace(/=/g, "");
    } else {
        return uint8arrayToBase64url(new Uint8Array(bytes));
    }
}
class WebAuthServerError extends Error {
    constructor(foo = 'bar', ...params) {
      super(...params)
      this.name = 'ServerError'
      this.foo = foo
      this.date = new Date()
    }
}
function throwError(response) {
    throw new WebAuthServerError("Error from client", response.body);
}
function checkStatus(response) {
    if (response.status !== 200) {
        throwError(response);
    } else {
        return response;
    }
}
function initialCheckStatus(response) {
    checkStatus(response);
    return response.json();
}
function followRedirect(response) {
    if (response.status == 200) {
        window.location.href = response.url;
    } else {
        throwError(response);
    }
}
function displayError(error) {
    const errorElem = document.getElementById('errors');
    errorElem.innerHTML = error;
    console.error(error);
}
