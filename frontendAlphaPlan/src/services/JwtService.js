import router from "../router/base";
import {customFetch} from "../utils/FetchUtil";

const baseUrl = import.meta.env.VITE_IP_BACKEND;
export async function login(password, email) {
    const response = await fetch(baseUrl + 'v1/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            "email": email,
            "password": password
        })
    });
    return await response.json();
}

export async function getTeamReferent(id) {
    try {
        const response = await customFetch(`${baseUrl}equipes/${id}/referent`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Erreur lors de la récupération du référent de l\'équipe.');
        }

        const responseText = await response.text();
        if (!responseText) {
            return null;
        }

        return JSON.parse(responseText);
    } catch (error) {
        console.error('Erreur:', error);
        throw error;
    }
}



export async function logout() {
    localStorage.clear();
    // Send a request to the backend to reset the token in the user's table
    fetch(import.meta.env.VITE_IP_BACKEND + 'v1/auth/logout', {
        method: 'POST'
    }).catch(function (error) {
        toast.error('Une erreur est survenue lors de la déconnexion');
        router.push({ name: 'REDIRECT' });
  });
}


export function isLogged() {
    return !!localStorage.getItem('token');
}