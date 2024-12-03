import {customFetch} from "../utils/FetchUtil";

export async function getNotifications(user_id) {
    try {
      const response = await customFetch('notification/utilisateur/' + user_id);
      console.log(response);
      if (response.ok) {
        return await response.json();
      } else {
        throw new Error('Erreur lors de la récupération des notifications');
      }
    } catch (error) {
    console.error('Erreur lors de la récupération des notifications :', error);
    throw error;
  }
}

export async function deleteNotification(id){
  try {
    const response = await customFetch('notification/' + id, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json'
      },
    });
    if (response.ok) {
      return await response.json();
    } else {
      console.error(response)
      throw new Error('Erreur lors de la suppression de la notification');
    }
  } catch (error) {
    console.error('Erreur lors de la suppression de la notification :', error);
    throw error;
  }
}

export const createNotification = async (type, message, envoyeur, receveur) => {
  try {
    const requestBodyNotif = {
      type: type,
      message: message,
      envoyeur: envoyeur,
      receveur: receveur
    };

    const requestOptionsNotif = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(requestBodyNotif)
    };

    const responseNotif = await customFetch('notification/creer', requestOptionsNotif);
    console.log(responseNotif);
    return responseNotif.json();
  } catch (error) {
    throw error;
  }
}