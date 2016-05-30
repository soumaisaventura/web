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
        this.download(this.getFileName(eventId + "-fichas", ".pdf"), this.url + "/" + eventId + "/registrations/form", callback);
    },

    exportDownload: function (eventId, callback) {
        this.download(this.getFileName(eventId + "-exportacao", ".xlsx"), this.url + "/" + eventId + "/registrations/export", callback);
    },

    download: function (filename, url, callback) {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", url);
        xhr.responseType = 'blob';
        xhr.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                var blob = new Blob([this.response], {
                    type: this.response.type
                });

                saveAs(blob, filename);
                callback();
            }
        };
        App.setHeader(xhr);
        xhr.send();
    },

    getFileName: function (prefix, sufix) {
        return prefix + moment().format("-YYYYMMDD-HHmmss") + sufix;
    }
};
