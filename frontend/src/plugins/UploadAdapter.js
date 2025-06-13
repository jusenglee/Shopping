import axios from '@/axios.js';
const noConextPathUrl = 'D:\\Data_Files/';

export default class UploadAdapter {
    constructor(loader, url) {
        this.url = url;
        this.loader = loader;
        this.loader.file.then((pic) => (this.file = pic));

        this.upload();
    }

    // Starts the upload process.
    upload() {
        return this.loader.file.then((uploadedFile) => {
            return new Promise((resolve, reject) => {
                const params = {
                    upload: uploadedFile,
                };
                axios.api
                    .fetchFileUpload(params)
                    .then((res) => {
                        const returnUrl = res.data.data.url;
                        resolve({
                            default: `${noConextPathUrl}${returnUrl}`,
                        });
                    })
                    .catch((error) => {
                        console.log(error);
                        reject(error.response.data.message);
                    });
            });
        });
    }
}
