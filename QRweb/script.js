let qrCode;
const qrContainer = document.getElementById('qrcode');
const sessionIdText = document.getElementById('session-id');

function generateSessionId() {
  const now = new Date().getTime();
  return 'QR-' + Math.random().toString(36).substr(2, 6).toUpperCase() + '-' + now;
}

function updateQRCode() {
  const sessionId = generateSessionId();
  qrContainer.innerHTML = '';
  qrCode = new QRCode(qrContainer, {
    text: sessionId,
    width: 256,
    height: 256,
    colorDark : "#000000",
    colorLight : "#ffffff",
    correctLevel : QRCode.CorrectLevel.H
  });
  sessionIdText.innerText = `Session ID: ${sessionId}`;
}

updateQRCode();
setInterval(updateQRCode, 4000);
