import { customFetch } from '../utils/FetchUtil';

export async function fetchEtudiants() {
  const response = await customFetch('etudiants/eleves');
  return await response.json();
}

export async function fetchRoles(etudiantId) {
  const response = await customFetch(`roles/${etudiantId}`);
  const data = await response.json();
  return data.length > 0 ? data : [{ nom: 'N/A' }];
}


export async function fetchMoyenne(equipeId) {
  try {
    const response = await customFetch(`notes/moyenne/${equipeId}`);
    if (response.ok) {
      const moyenne = await response.text();
      const moyenneNumber = parseFloat(moyenne);
      return !isNaN(moyenneNumber) ? moyenneNumber.toFixed(2) : 'N/A';
    } else {
      console.error('Erreur', equipeId);
      return 'N/A';
    }
  } catch (error) {
    console.error('Erreur', equipeId, ':', error);
    return 'N/A';
  }
}