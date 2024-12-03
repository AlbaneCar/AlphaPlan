import router from "../router/base";

const baseUrl = import.meta.env.VITE_IP_BACKEND;

export async function customFetch(uri, options) {
    const modifiedOptions = {
        ...options,
        headers: {
            ...(options?.headers || {}),
            ...(localStorage.getItem('token') ? {'Authorization': 'Bearer ' + localStorage.getItem('token')} : {})
        }
    };

    const url = uri.includes('http') ? uri : baseUrl + uri;
    const response = await fetch(url, modifiedOptions);
    if(response.status === 403) {
        const json = await response.json()
        if(json.message === 'Token invalid') {
            localStorage.clear();
            fetch(baseUrl + 'v1/auth/logout', {
                method: 'POST',
            }).then(() => {
                    router.push({name: 'REDIRECT'});
                }
            );
        }
    }

    return response;
}