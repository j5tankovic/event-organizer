const functions = require('firebase-functions');

const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

exports.pushNotification = functions.database.ref('events/{eventId}/invitations/{invitationId}').onWrite((change, context) => {
    console.log('Push notification event triggered');
    
    console.log('Before', change.before.val());
    console.log('After', change.after.val());

    const valueObject = change.after.val();

    const userMail = valueObject.invitedUser.email.replace("@", "_");
    console.log('User id', userId);

    const payload = {
        notification: {
            title: 'Event organizer',
            body: 'Poziv na dogadjaj',
            sound: 'default'
        },
        data: {
            title: "Poziv na dogadjaj",
            message: "Dobrodosli"
        }
    };

    return admin.messaging().sendToTopic(`eventInvitationFor-${userMail}`, payload);
});
