import { customFetch } from '../utils/FetchUtil';


export async function saveNoteToDatabase(noteEquipe) {
    try {
        const response = await customFetch('v1.0/notesEquipe/evaluerMultiple', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(noteEquipe),
        });

        console.log('Contenu de la réponse:', response.status);

        if (!response.ok) {
            throw new Error('Erreur pour enregistrer la note dans la base de données');
        }
        const data = await response.json();
        console.log('Réponse du serveur:', data);

        return response;

    } catch (error) {
        console.error('Erreur lors de la soumission de la note', error);
        throw error;
    }
}

export async function saveNoteIndividuelleToDatabase(noteIndividuelle) {
    try {
        const response = await customFetch('v1.0/notesEleve/evaluer', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(noteIndividuelle),
        });

        console.log('Contenu de la réponse:', response.status);

        if (!response.ok) {
            throw new Error('Erreur pour enregistrer la note indivielle dans la base de données');
        }
        const data = await response.json();
        console.log('Réponse du serveur:', data);

        return response;

    } catch (error) {
        console.error('Erreur lors de la soumission de la note individuelle', error);
        throw error;
    }
}



// Fonction pour récupérer les notes d'une équipe
export async function getNoteFromDatabase(idEquipe, idSprint, idUser) {
    try {
        const response = await customFetch('v1.0/notesEquipe/byEquipe/' + idEquipe + '/' + idSprint + '/' + idUser,{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });
        if (!response.ok) {
            throw new Error('Erreur pour récupérer les notes de l\'équipe');
        }
        return await response.json();
    } catch (error) {
        console.error('Erreur lors de la récupération des notes de l\'équipe', error);
        throw error;
    }
}

// Fonction pour récupérer les notes indiviuelles d'un élève

export async function getNoteIndividuelleFromDatabase(idEleve, idSprint, idUser) {
    try {
        const response = await customFetch('v1.0/notesEleve/byEleve/' + idEleve + '/' + idSprint + '/' + idUser,{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        console.log('Contenu de la réponse:', response.status);

        if (!response.ok) {
            throw new Error('Erreur pour récupérer les notes individuelles');
        }
        const data = await response.json();
        console.log('Réponse du serveur:', data);

        return data;

    } catch (error) {
        console.error('Erreur lors de la récupération des notes individuelles', error);
        throw error;
    }
}
