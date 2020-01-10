window.openVidu = null;
window.sessionName = null;
window.roomSession = null;
window.publisher = null;

window.joinSession = function (sessionData) {
    let data = JSON.parse(sessionData);
    console.log(sessionData);

    sessionName = data.sessionName;

    openVidu = new OpenVidu();

    roomSession = openVidu.initSession();

    roomSession.on('streamCreated', (event) => {
        let subscriber = roomSession.subscribe(event.stream, 'video-container');
        subscriber.on('videoElementCreated', (event) => {
            appendUserData(event.element, subscriber.stream.connection);
        });
    });
    roomSession.on('streamDestroyed', (event) => {
        removeUserData(event.stream.connection);
    });
    let nickname = data.name;
    let token = data.token;
    console.log('token ' + token)
    roomSession.connect(token, {clientData: nickname})
        .then(() => {
            publisher = openVidu.initPublisher('video-container', {
                audioSource: undefined,
                videoSource: undefined,
                publishAudio: true,
                publishVideo: true,
                resolution: '640x480',
                mirror: false
            });
            publisher.on('videoElementCreated', (event) => {
                let userData = {
                    nickName: nickname
                };
                appendUserData(event.element, userData);
                $(event.element).prop('muted', true); // Mute local video
            });
            roomSession.publish(publisher);
        })
        .catch(error => {
            console.warn('There was an error connecting to the session:', error.code, error.message);
        });
};

window.appendUserData = function (videoElement, connection) {
    let clientData;
    let nodeId;
    if (connection.nickName) { // Appending local video data
        clientData = connection.nickName;
        nodeId = 'main-videodata';
    } else {
        clientData = JSON.parse(connection.data.split('%/%')[0]).clientData;
        nodeId = connection.connectionId;
    }
    let dataNode = document.createElement('div');
    dataNode.className = "data-node";
    dataNode.id = "data-" + nodeId;
    dataNode.innerHTML = "<p class='nickName'>" + clientData + "</p>";
    videoElement.parentNode.insertBefore(dataNode, videoElement.nextSibling);
};

window.leaveSession = function () {
    console.log('disconnecting session');
    roomSession.disconnect();
    console.log(roomSession);
    cleanSessionView();
};

window.onbeforeunload = () => {
    if (roomSession) {
        console.log('before unload');
        leaveSession();
    }
};

window.removeUserData = function (connection) {
    var userNameRemoved = $("#data-" + connection.connectionId);
    userNameRemoved.remove();
};

window.cleanSessionView = function () {
    $('#main-video video').css("background", "");
};