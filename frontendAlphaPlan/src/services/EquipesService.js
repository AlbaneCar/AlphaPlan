import { customFetch } from '../utils/FetchUtil';


const baseUrl = import.meta.env.VITE_IP_BACKEND;


export async function fetchEquipes() {
  const response = await customFetch('v1.0/equipes');
  const data = await response.json();
  // Boucle pour aller chercher les membres de chaque équipe 
  for (const equipe of data) {
    const response = await customFetch(`users/equipes/${equipe.id}`);
    equipe.membres = await response.json();
  }

  //Boucle pour aller chercher la moyenne de chaque équipe
  for (const equipe of data) {
    const response2 = await customFetch(`notes/moyenne/${equipe.id}`);
    equipe.moyenne = await response2.text();
  }
  return data.map(equipe => ({
    ...equipe,
  }));
}

export async function fetchEtudiants() {
  const response = await customFetch('etudiants/eleves');
  return await response.json();
}


export async function fetchRoles(etudiantId) {
  const response = await customFetch(`roles/${etudiantId}`);
  const data = await response.json();
  return data || { nom: 'Non déterminé' };
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

export async function fetchOLPL() {
  try {
    const response = await customFetch(`users/admins`);
    if (response.ok) {
      return response.json();
    } else {
      console.error('Erreur');
      return 'N/A';
    }
  } catch (error) {
    console.error(error);
    return 'N/A';
  }
}
