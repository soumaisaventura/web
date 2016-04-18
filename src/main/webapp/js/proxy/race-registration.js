var RaceRegistrationProxy = {

    url: App.getContextPath() + "/api/events",

    submitRegistration: function (raceId, eventId, data) {
        return $.ajax({
            type: "POST",
            url: this.url + "/" + eventId + "/races/" + raceId + "/registrations",
            data: JSON.stringify(data),
            contentType: "application/json",
            beforeSend: function (request) {
                App.setHeader(request)
            }
        });
    },

    formDownload: function (eventId, callback) {
        this.download(this.url + "/" + eventId + "/registrations/form", callback);
    },

    exportDownload: function (eventId, callback) {
        this.download(this.url + "/" + eventId + "/registrations/export", callback);
    },

    download: function (url, callback) {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", url);
        xhr.responseType = 'blob';
        xhr.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                var blob = new Blob([this.response], {
                    type: this.response.type
                });

                var url = (window.URL || window.webkitURL).createObjectURL(blob);
                location.href = url;
                callback();
            }
        };
        App.setHeader(xhr);
        xhr.send();
    }
};
