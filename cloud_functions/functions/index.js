const functions = require('firebase-functions');

const admin = require('firebase-admin');

const express = require('express');

admin.initializeApp(functions.config().firebase);

exports.pushNotification = functions.database.ref('events/{eventId}/invitations/{invitationId}').onCreate((snapshot, context) => {
    console.log('Invitation created - Push notification event triggered');
    
    // console.log('Before', change.before.val());
    // console.log('After', change.after.val());

    const valueObject = snapshot.val();

    const userMail = valueObject.invitedUser.email.replace("@", "_");
    console.log('User mail', userMail);

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

exports.notifyAboutPresence = functions.database.ref('events/{eventId}/invitations/{invitationId}').onUpdate((change, context) => {
    console.log('Invitation accepted/rejected - Push notification event triggered');
    
    console.log('Before', change.before.val());
    console.log('After',  change.after.val());
    
    console.log('Parent', change.ref.parent.parent.child('creator').email);

    const valueObject = snapshot.val();
    const status = valueObject.status;

    const creatorMail = change.ref.parent.parent.child('creator').email.replace("@", "_");
    console.log('User mail', creatorMail);

    const payload = {
        notification: {
            title: 'Event organizer',
            body: `${status}`,
            sound: 'default'
        },
        data: {
            title: "Poziv na dogadjaj",
            message: "Dobrodosli"
        }
    };

    return admin.messaging().sendToTopic(`eventConfirmationFor-${creatorMail}`, payload);
});


const app = express();
app.get('/:id', (req, res) => res.send("Tralalala" + req.params.id));
app.get('/events/:event/invitations/:invitation/status', (req,res) => {
	//res.send("Requst: " + "/events/:event/invitations/" + req.params.invitation + "/status" + req.param.status)
    
    return admin.database().ref('/events/" + req.params.event + "/invitations/" + req.params.invitation + "/status')
      .once('value').then(snapshot => {
        // there, I queried!
        return res.send("SNAPSHOT: " + snapshot);
      });
});

exports.invitationAccepting = functions.https.onRequest(app);
