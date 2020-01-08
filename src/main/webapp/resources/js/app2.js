var openVidu;
var session;

var sessionName;	// Name of the video session the user will connect to
var token;			// Token retrieved from OpenVidu Server


/* OPENVIDU METHODS */
function joinSession(sessionData) {
    let data = JSON.parse(sessionData);
    sessionName = data.sessionName;

    openVidu = new OpenVidu();
    session = openVidu.initSession();
    session.on('streamCreated', (event) => {
        let subscriber = session.subscribe(event.stream, 'video-container');
        subscriber.on('videoElementCreated', (event) => {
            appendUserData(event.element, subscriber.stream.connection);
        });
    });
    session.on('streamDestroyed', (event) => {
        removeUserData(event.stream.connection);
    });
    let nickname = data.name;
    let token = data.token;
    session.connect(token, {clientData: nickname})
        .then(() => {
            let userName = $("#user").val();
            $('#session-title').text(sessionName);
            $('#main-form').hide();
            $('#session').show();

            let publisher = openVidu.initPublisher('main-video', {
                audioSource: undefined,
                videoSource: undefined,
                publishAudio: true,
                publishVideo: true,
                resolution: '640x480',
                mirror: false
            });
            publisher.on('videoElementCreated', (event) => {
                let userData = {
                    nickName: nickname,
                    userName: userName
                };
                appendUserData(event.element, userData);
                $(event.element).prop('muted', true); // Mute local video
            });
            session.publish(publisher);
        })
        .catch(error => {
            console.warn('There was an error connecting to the session:', error.code, error.message);
        });
}

function leaveSession() {
    session.disconnect();
    session = null;
    cleanSessionView();

    $('#main-form').show();
    $('#session').hide();
}

window.onbeforeunload = () => { // Gracefully leave session
    if (session) {
        leaveSession();
    }
};

function appendUserData(videoElement, connection) {
    var clientData;
    var nodeId;
    if (connection.nickName) { // Appending local video data
        clientData = connection.nickName;
        nodeId = 'main-videodata';
    } else {
        clientData = JSON.parse(connection.data.split('%/%')[0]).clientData;
        nodeId = connection.connectionId;
    }
    var dataNode = document.createElement('div');
    dataNode.className = "data-node";
    dataNode.id = "data-" + nodeId;
    dataNode.innerHTML = "<p class='nickName'>" + clientData + "</p>";
    videoElement.parentNode.insertBefore(dataNode, videoElement.nextSibling);
}

function removeUserData(connection) {
    var userNameRemoved = $("#data-" + connection.connectionId);
    userNameRemoved.remove();
}

function cleanSessionView() {
    $('#main-video video').css("background", "");
}

function executeRemote() {
    rc();
}