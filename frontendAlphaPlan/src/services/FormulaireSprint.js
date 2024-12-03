import { customFetch } from '../utils/FetchUtil';

export async function fetchData(allSprintsData) {
    try {
        const response = await customFetch('sprints/form', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(allSprintsData) // Envoyez toutes les données de sprint
        });
        if (!response.ok) {
            throw new Error('Erreur lors de l\'envoi du formulaire');
        }
        return response
    } catch (error) {
        console.error('Erreur lors de la soumission du formulaire:', error);
        throw error;
    }
}
