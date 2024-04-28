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
const tokenDispositivo = 'fDITm-SjR2C_Eg01SOfHZK:APA91bGU1DiDoKHHEXm-2IdIA053FZ7BTwDTdQLL_GOuUe_qmpNLf_55oMykDI77gEs1-S-zn3WM10RrilKEBD7USt6NtkIxW3YmTcxAjoUI15WsrNkXYb1Z_U1w-c2JpJV6ipqF_V4d';
const mensaje = '¡Hola desde el servidor!';

enviarMensajePush(tokenDispositivo, mensaje);
