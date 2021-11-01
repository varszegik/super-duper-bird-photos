const axios = require("axios");

exports.handler = async (event) => {
  const reqBody = JSON.parse(event.body);
  try {
    const headers = {
      "Content-Type": "application/json",
      Authorization:
        "key=AAAASMq_i7g:APA91bHB0luNC3If98zMGASZoRQ_5PmEVLXIWkEms60xlZwegr_fjqBPgUou_n00vKgKjSwnCiSdaz4WY5oXJLjiDyvQAoWIyiQ3pzFDJaUUPho2Lm0N-uOwXWFTXr47uYCSehY0Bgie",
    };

    const body = {
      to: `/topics/${reqBody.topic}`,
      notification: {
        title: "Bird spotted",
        body: `A ${reqBody.species} in your birdhouse has been spotted!`,
        mutable_content: true,
        sound: "Tri-tone",
      },
    };

    await axios.post("https://fcm.googleapis.com/fcm/send", body, { headers });
  } catch (err) {
    console.log(err);
  }
  const response = {
    statusCode: 200,
    body: JSON.stringify(event),
  };
  return response;
};
