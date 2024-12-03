import { customFetch } from '../utils/FetchUtil';

export async function fetchSprint() {
    const response = await customFetch('sprints/');

    const data = await response.json();
    return data.map(sprint => ({
        ...sprint
    }));
}

export async function fetchCurrentSprint() {
    const response = await customFetch('sprints/current');
    return await response.json();
}

export async function getAllSprints(){
    const response = await customFetch('sprints');
    return await response.json();
}

export async function updateSprint(sprint) {
    const requestOptions = {method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(sprint)
    };
    const response = await customFetch('sprints/update', requestOptions);
    return await response.json();
}