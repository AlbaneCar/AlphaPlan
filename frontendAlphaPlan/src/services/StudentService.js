import { customFetch } from '../utils/FetchUtil';

export async function fetchStudents() {
    try {
      const response = await customFetch('etudiants/eleves');
      return await response.json();
    } catch (error) {
      console.error('Erreur lors de la récupération des étudiants :', error);
      return [];
    }
  }
