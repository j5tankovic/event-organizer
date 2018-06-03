const functions = require('firebase-functions');

const admin = require('firebase-admin');

const express = require('express');

admin.initializeApp(functions.config().firebase);

exports.pushNotification = functions.database.ref('events/{eventId}/invitations/{invitationId}').onCreate((snapshot, context) => {
    console.log('Invitation created - Push notification event triggered');

    const valueObject = snapshot.val();

    const creator = valueObject.event.creator;
    console.log('Creator', creator.name);
    const event = valueObject.event.name;
    const userMail = valueObject.invitedUser.email.replace("@", "_");
    console.log('User mail', userMail);

    const payload = {
        notification: {
            title: 'Event organizer',
            body: `${creator.name} ${creator.lastName} has invited you to event: ${event}`,
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

    const status = change.after.val().status;

    const event = change.after.val().event.name;
    const creatorMail = change.after.val().event.creator.email.replace('@', '_');
    const invitedUser = change.after.val().invitedUser;
    console.log('Invited user', invitedUser.name);

    const payload = {
        notification: {
            title: 'Event organizer',
            body: `${invitedUser.name} ${invitedUser.lastName} has ${status.toLowerCase()} your invitation
            for event ${event}`,
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
app.get('/events/:event/invitations/:invitation/:status', (req,res) => {
	const newStatus = {status : req.params.status};
	const statusPath = '/events/' + req.params.event +
		"/invitations/" + req.params.invitation;
	return admin.database().ref(statusPath).update(newStatus).then(function() {
		  return res.redirect(303, "https://firebasestorage.googleapis.com/v0/b/event-organizer-ftn.appspot.com/o/InvitationAcceptationPage.html?alt=media&token=71516f48-ab46-45b9-9f33-90fe1508accd");
		}).catch(function(error) {
		  return res.send("Status updating failed");
		});
	});

exports.invitationAccepting = functions.https.onRequest(app);
