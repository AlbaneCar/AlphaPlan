import { customFetch } from '../utils/FetchUtil';

export async function createTeamNote(noteEquipe) {
  const requestOptions = {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify(noteEquipe)
  };
  try {
    const response = await customFetch(`v1.0/notesEquipe/evaluer`, requestOptions);
    if (!response.ok) {
      throw new Error("Erreur lors de l'ajout de la note d'équipe");
    }
    return await response.json();
  } catch(error) {
    throw new Error("Erreur lors de l'ajout de la note d'équipe");
  }
}