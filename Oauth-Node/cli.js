const axios = require('axios');
const id = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6ImJXOFpjTWpCQ25KWlMtaWJYNVVRRE5TdHZ4NCJ9.eyJ2ZXIiOiIyLjAiLCJpc3MiOiJodHRwczovL2xvZ2luLm1pY3Jvc29mdG9ubGluZS5jb20vOTE4ODA0MGQtNmM2Ny00YzViLWIxMTItMzZhMzA0YjY2ZGFkL3YyLjAiLCJzdWIiOiJBQUFBQUFBQUFBQUFBQUFBQUFBQUFJS2dqNnd6cGZseWtIX1pYdnZjUVJJIiwiYXVkIjoiODdkZmYzN2YtYWIwNS00MjBlLThmNDItNTVhM2U1ZDNlZmNjIiwiZXhwIjoxNjgzNDk1MDk0LCJpYXQiOjE2ODM0MDgzOTQsIm5iZiI6MTY4MzQwODM5NCwidGlkIjoiOTE4ODA0MGQtNmM2Ny00YzViLWIxMTItMzZhMzA0YjY2ZGFkIiwibm9uY2UiOiI3NzE1bWcwY3dociIsImFpbyI6IkRXaGUyRktPRExKV3AxV1Y1Nm50N0gySDdZVE1jQVNqQkxjVmRGOGVFdjFFSmJkNXpZa2V4S0NkciFpMDFlaFZFSUFLRzlwUlFyUWlLQ2Y4T3Btdmw4UHJSdXlIbFNnVEwyMypvIXdwY2plOVlFSG1rdmp3NkJ4UmYhU0VNcEJIQ0c1MXhBdjk2Y2pIWERZcCFFdHBJenhWNFJsR3VCODNFOTVDVUNHUjRSS3pRSEtFbWxFcUNYSUtXa0prbHlGWEZRJCQifQ.KvyNfQWPG7AbBkKtxVPsneH2u2H2T-RTh9gZObNV9HMjgR6a3_BGFMV8uIA8QJN8b3ymoo_irncfxYLMQwjPGIo9yX9pgl34_HWHGIOEAlXe9ixDGSomrKOVKH0VrClWWe47tZYHKT9_DFb4ezlj2dTATzPwV6XuZKinNT3mXAbYhVBcsZQsO4bsMjhRcIoDPGJLjfIIis6DgmbC9MLsUTD6GR2i0OCC4S_3_4nRvGlKXY8Ak-NUQJGDaT7IP1jkVDgixtNFx8L9Yruyr6GPo72MwXghjSnlzT00J5aQSbrDMrpoFT0YyeY_myqNJfaOT2VIR23Cc9LTqYB-pcw-3A'

const CLIENT_ID = '989d832b88b05049e5ac';
//const CLIENT_ID = '87dff37f-ab05-420e-8f42-55a3e5d3efcc ';

async function run(token) {
    console.log(token);
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

async function microsoft(){


    const response = await(axios.post(
        'https://graph.microsoft.com//oidc/userinfo',
        {
        'Authorization': 'Bearer ' + id},

    ))
    sleep(5000).then(run(response))
    }




async function main() {
    const {
        device_code,
        user_code,
        verification_uri,
        expires_in,
        interval,
    } = (await axios.post(
        'https://github.com/login/device/code',
        {
            client_id: CLIENT_ID,
            scope: 'public_repo user:email',
        },
        {
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            },
        },
    )).data;

    const expireDate = Date.now() + (expires_in * 1000);

    // Informar usuario
    console.log('Acesse essa url: ' + verification_uri);
    console.log('E informe o código: ' + user_code);

    const intervalHandler = setInterval(async () => {
        const currentDate = Date.now();
        if (currentDate >= expireDate) {
            clearInterval(intervalHandler);
            console.error('Authentication expired');
            return;
        }

        const { data } = await axios.post(
            'https://github.com/login/oauth/access_token',
            {
                grant_type: 'urn:ietf:params:oauth:grant-type:device_code',
                client_id: CLIENT_ID,
                device_code: device_code,
            },
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                },
            },
        );

        if (data.error) {
            switch (data.error) {
                case 'authorization_pending':
                    console.log('Waiting for authorization...');
                    break;
                case 'expired_token':
                    clearInterval(intervalHandler);
                    console.error('Authentication expired');
                    break;
                default:
                    clearInterval(intervalHandler);
                    console.error('Unknown error', data);
                    break;
            }
        } else {
            clearInterval(intervalHandler);
            run(data);
        }
    }, interval * 2000);
}

microsoft();