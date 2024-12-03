import { customFetch } from '../utils/FetchUtil.js';


export async function fetchBMEquipe(equipe_id, sprint_id) {
  try {
    const response = await customFetch(`v1.0/notesEleve/listeBmEquipe/${equipe_id}/${sprint_id}`);
    if (response.ok) {
      return await response.json();
    }
  } catch (error) {
    throw new Error(`Erreur lors de la récupération des bonus malus de l'équipe`);
  }
}

export async function createBM(eleve_id, evaluateur_id, sprint, valeur, commentaire) {
  const body = [{
    eleve: { id: eleve_id },
    evaluateur: { id: evaluateur_id },
    sprint: { id: sprint },
    note: valeur,
    commentaire: commentaire,
    typeNoteEleve: "SS_BM"
  }];

  try {
    const response = await customFetch(`v1.0/notesEleve/evaluer`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body)
    });

    console.log(await response.json());

    if (!response.ok) {
      throw new Error("Erreur lors de l'ajout du bonus malus");
    }
    else{
      return response;
    }
  }catch(error){
    throw new Error("Erreur lors de l'ajout du bonus malus");
  }
}

export async function modifyBM(id, valeur, commentaire) {
  const body = {
    note: valeur,
    commentaire: commentaire
  };

  try {
    const response = await customFetch(`v1.0/notesEleve/${id}`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body)
    });

    if (!response.ok) {
      throw new Error("Erreur lors de la modification du bonus malus");
    }
    else{
      return response;
    }
  }catch(error){
    throw new Error("Erreur lors de la modification du bonus malus");
  }
}

export async function deleteBM(id) {
    const url = `v1.0/notesEleve/${id}`;
    try {
      const response = await customFetch(url, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json'
        },
      });
  }
  catch(error){
    throw new Error("Erreur lors de la suppression du bonus malus");
  }
}