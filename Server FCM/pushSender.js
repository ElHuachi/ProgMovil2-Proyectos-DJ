const admin = require('firebase-admin');

// Inicializar la app de Firebase Admin con tus credenciales
const serviceAccount = require('./fcm-definitivo-firebase-adminsdk-aidob-954a3f1a8b');
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

// Función para enviar un mensaje push a un dispositivo Android específico
async function enviarMensajePush(token, mensaje) {
  try {
    const message = {
      token: token,
      notification: {
        title: 'Título del mensaje',
        body: mensaje
      }
    };

    const response = await admin.messaging().send(message);
    console.log('Mensaje enviado exitosamente:', response);
  } catch (error) {
    console.error('Error al enviar el mensaje:', error);
  }
}

// Uso de la función para enviar un mensaje push
const tokenDispositivo = 'dUVNVRPaR7u1Z89-coILMK:APA91bFKHY1dYKoaEXZM4CNktb2So_7jbwJzmxmDhlODPCqvAfae15rM3MkgVAWovhvpl34B3j_UZRzLylRtOsOCUZRSvCBZTpy9psheE3deW0M0Jo_peIoXKURCzPxK0fwVBXhJpi9A';
const mensaje = '¡Hola desde el servidor!';

enviarMensajePush(tokenDispositivo, mensaje);
