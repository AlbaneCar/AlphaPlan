import { customFetch } from '../utils/FetchUtil';

export async function fetchUsers() {
    try {
      const response = await customFetch('etudiants/teachers');
      return await response.json();
    } catch (error) {
      console.error('Erreur lors de la récupération des utilisateurs :', error);
      return [];
    }
  }

  export async function deleteUser(userId) {
    try {
      const response = await customFetch(`etudiants/supprimer/${userId}`, {
        method: 'DELETE',
      });
      if (response.ok) {
        return true;
      } else {
        console.error("Echec");
        return false;
      }
    } catch (error) {
      console.error("Erreur :", error);
      return false;
    }
  }

  export async function addTeacher(payload) {
    try {
      const response = await customFetch('etudiants/ajouter', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload)
      });
      if (response.ok) {
        return true;
      } else {
        console.error('Échec de l\'ajout de l\'encadrant');
        return false;
      }
    } catch (error) {
      console.error('Erreur lors de l\'ajout de l\'encadrant :', error);
      return false;
    }
  }

