import { customFetch } from '../utils/FetchUtil';



export async function fetchDataProjectCreation() {
    try {
        const response = await customFetch(`etudiants/eleves`);
        const response2 = await customFetch(`equipes`);
        const response3 = await customFetch(`sprints/nombre`);
        if (response.ok && response2.ok && response3.ok) {
            const r1 = (await response.json()).length;
            const r2 = (await response2.json()).length;
            const r3 = (await response3.json());
            const array =  [r1 != 0, r2 != 0, r3 != 0];
            let count = 0;
            for (let i = 0; i < array.length; i++) {
                if (array[i]) {
                    count++;
                } else {
                    break;
                }
            }
            return count;
        }
    } catch (error) {
        throw new Error('Erreur lors de la récupération des données pour la création de projet');
    }
}