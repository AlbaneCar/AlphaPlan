import { customFetch } from "../utils/FetchUtil";
const baseUrl = import.meta.env.VITE_IP_BACKEND;

export async function getConversations(){
    const response = await customFetch('conversation');
    return await response.json();
}

export async function getConversationsTeam(equipeId){
    const response = await customFetch(`conversation/equipe/${equipeId}`);
    return await response.json();
}

export async function getConversationsUser(userId){
    const response = await customFetch(`conversation/utilisateur/${userId}`);
    // Si la réponse à un status 204, cela signifie qu'il n'y a pas de conversation pour cet utilisateur
    if(response.status === 204){
        return [];
    }
    return await response.json();
}

export const createConv = async (titre, auteurId, participantsId) => {
    try {
        const requestBody = {
            titre: titre,
            auteur: auteurId,
            participants: participantsId
        };

        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody)
        };
        const response = await customFetch('conversation/creer', requestOptions);
        return response.json();
    }
    catch (error) {
        throw error;
    }
};

export async function getMessages(conversationId) {
    const response = await customFetch(`conversation/${conversationId}`);
    return await response.json();
}

export const sendMessage = async (conversationId, message, etudiantId) => {
    try {
        const requestBody = {
            message: message,
            auteur: { id: etudiantId }
        };

        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody)
        };

        const response = await customFetch(`conversation/${conversationId}/repondre`, requestOptions);
        if (!response.ok) {
            throw new Error('Erreur lors de la requête.');
        }

        return true;

    } catch (error) {
        throw error;
    }
}

export const sendNotif = async (teamMembers, etudiantid) => {
    try {
        const requests = teamMembers.map(async (member) => {
            const requestBodyNotif = {
                type: "NOUVEAU_MESSAGE",
                message: "Nouveau message concernant votre équipe",
                envoyeur: {id: etudiantid},
                receveur: {id: member}
            };

            const requestOptionsNotif = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(requestBodyNotif)
            };

            const responseNotif = await customFetch('notification/ajouter', requestOptionsNotif);
            console.log(responseNotif);

            if (!responseNotif.ok) {
                throw new Error('Erreur lors de la requête.');
            }

        });

        await Promise.all(requests);

        return true;

    } catch (error) {
        throw error;
    }
};

export const sendNotifCreation = async (teamMembers, etudiantid) => {
    try {
        console.log(etudiantid);
        console.log(teamMembers);
        const requests = teamMembers.map(async (member) => {
            const requestBodyNotif = {
                type: "NOUVELLE_CONVERSATION",
                message: "Nouvelle conversation pour votre équipe",
                envoyeur: {id: etudiantid},
                receveur: {id: member}
            };

            const requestOptionsNotif = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(requestBodyNotif)
            };

            const responseNotif = await customFetch('notification/ajouter', requestOptionsNotif);
            console.log(responseNotif);

            if (!responseNotif.ok) {
                throw new Error('Erreur lors de la requête.');
            }

        });

        await Promise.all(requests);

        return true;

    } catch (error) {
        throw error;
    }
};

export const suppConversation = async (conversationId) => {
    try {
        const response = await customFetch(`conversation/${conversationId}`, {
            method: 'DELETE',
        });
        if (!response.ok) {
            throw new Error('Erreur lors de la requête.');
        } else {
            return response;
        }
    } catch (error) {
        throw error;
    }
  };