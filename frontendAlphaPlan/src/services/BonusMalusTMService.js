import { customFetch } from '../utils/FetchUtil.js';



export async function getTeamName(teamId) {
  try {
    const response = await customFetch(`v1.0/equipes/${teamId}`);
    return await response.json();
  } catch (error) {
    console.error('Error fetching team data:', error);
    throw error;
  }
}



export async function getTeamInformations(teamId) {
    try {
      const response = await customFetch(`users/equipes/${teamId}`);
      const data = await response.json();
      data.forEach(member => {
        member.bonusMalus = 0;
        member.note_tebm;
      });

      return data;
    } catch (error) {
      console.error('Error fetching team data:', error);
      throw error;
    }
}

export async function getNoteTeBM(userId, sprintNumber) {
  try {
      const response = await customFetch(`v1.0/notesEleve/${userId}/TE_BM?sprint=${sprintNumber}`);
      return await response.json();
  } catch (error) {
  }
}

export async function getBMNote(noteId) {
  if (typeof noteId === 'undefined') {
    return;
  }
  try {
      const response = await customFetch(`v1.0/bonusMalus/note/${noteId}`);
      return await response.json();
  } catch (error) {
  }
}

export async function getBonusMalusList(teamId, sprintNumber) {
  try {
    const response = await customFetch(`v1.0/bonusMalus/${teamId}/liste?sprint=${sprintNumber}`);
    const data = await response.json();
    const nbResponses = data.length;
    return { bonusMalusList: data, nbResponses };
  } catch (error) {
    console.error('Error fetching bonus/malus list:', error);
    throw error;
  }
}

export const addBonusMalus = async (teamMembers, comment, id) => {
  try {
      const requests = teamMembers.map(async (member) => {
          const requestBody = {
              valeur: member.bonusMalus,
              noteEleve: { id: member.note_tebm.id },
              commentaire: comment,
              evaluateur: { id: id}
          };

          const requestOptions = {
              method: 'POST',
              headers: {
                  'Content-Type': 'application/json',
              },
              body: JSON.stringify(requestBody)
          };

          const requestBodyNotif = {
              type: "NOUVEAU_BM",
              message: "Un bonus malus vient d'être proposé",
              envoyeur: {id: id},
              receveur: {id: member.id}
          };

          const requestOptionsNotif = {
              method: 'POST',
              headers: {
                  'Content-Type': 'application/json',
              },
              body: JSON.stringify(requestBodyNotif)
          };

          const response = await customFetch(`v1.0/bonusMalus/ajouter`, requestOptions);
          const responseNotif = await customFetch(`notification/ajouter`, requestOptionsNotif);

          if (!response.ok || !responseNotif.ok) {
              throw new Error('Erreur lors de la requête.');
          }
      });

      await Promise.all(requests);
  } catch (error) {
      console.error(`Une erreur s'est produite lors de l'ajout des bonus/malus :`, error);
      throw error;
  }
};

export const modifyNoteEleve = async (teamMembers, idConnecte) => {
  try {
    const requests = teamMembers.map(async (member) => {
        const requestBody = {
            note: member.bonusMalus,
            commentaire: "Décision d'équipe"
        };

        const requestOptions = {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody)
        };

        const requestBodyBM = {
            valide: true
        };

        const requestOptionsBM = {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBodyBM)
        };

        const requestBodyNotif = {
            type: "BM_ACCEPTE",
            message: "Un bonus/malus vient d'être validé",
            envoyeur: {id: idConnecte},
            receveur: {id: member.id}
        };

        const requestOptionsNotif = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBodyNotif)
        };

        const response = await customFetch(`v1.0/notesEleve/${member.note_tebm.id}`, requestOptions);
        const responseBM = await customFetch(`v1.0/bonusMalus/${member.bm_note.id}`, requestOptionsBM);
        const responseNotif = await customFetch('notification/ajouter', requestOptionsNotif);

        if (!response.ok || !responseBM.ok || !responseNotif.ok) {
            throw new Error('Erreur lors de la requête.');
        }
    });

    await Promise.all(requests);

    return true;
  } catch (error) {
    console.error(`Une erreur s'est produite:`, error);
    throw error;
  }
}

export const addNoteTEBM = async (teamMembers, sprintNumber, note) => {
    try {
        const requestBody = teamMembers.map(member => ({
            note: note,
            eleve: { id: member.id },
            evaluateur: { id: member.id },
            sprint: { id: sprintNumber },
            typeNoteEleve: "TE_BM",
            commentaire: "Bonus/Malus initial"
        }));

        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody)
        };

        const response = await customFetch(`v1.0/notesEleve/evaluer`, requestOptions);

        console.log(response);

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Erreur lors de la requête: ${response.status} ${response.statusText} - ${errorText}`);
        }

        console.log(await response.json());

    } catch (error) {
        console.error(`Une erreur s'est produite:`, error);
        throw error;
    }
  };


export const NotifSuppBM = async (teamMembers, id) => {
  try {
      const requests = teamMembers.map(async (member) => {
          const requestBodyNotif = {
              type: "BM_SUPPRIME",
              message: "Un membre de l'équipe n'a pas accepté le bonus/malus proposé",
              envoyeur: {id: id},
              receveur: {id: member.id}
          };

          const requestOptionsNotif = {
              method: 'POST',
              headers: {
                  'Content-Type': 'application/json',
              },
              body: JSON.stringify(requestBodyNotif)
          };

          const responseNotif = await customFetch('notification/ajouter', requestOptionsNotif);

          if (!responseNotif.ok) {
              throw new Error('Erreur lors de la requête.');
          }

      });

      await Promise.all(requests);
  } catch (error) {
      console.error('Une erreur s\'est produite lors de la notif :', error);
      throw error;
  }
};

export async function getNbBMValides(id, sprint, equipe, type) {
  try {
      const response = await customFetch(`v1.0/validationbm/${id}/${type}?sprint=${sprint}&equipe=${equipe}`);
      const data = await response.json();
      return data;
  } catch (error) {
  }
}

export const validateBonusMalus = async (teamMembers, idUtilisateur) => {
  try {
      const requests = teamMembers.map(async (member) => {
          const requestBody = {
              bonusMalus: { id: member.bm_note.id },
              utilisateur: { id: idUtilisateur }
          };

          const requestOptions = {
              method: 'POST',
              headers: {
                  'Content-Type': 'application/json',
              },
              body: JSON.stringify(requestBody)
          };

          const response = await customFetch('v1.0/validationbm/ajouter', requestOptions);

          if (response.status === 204) {
              return false;
          } else if (response.status === 201) {
              return true;
          } else {
              throw new Error('Erreur lors de la requête.');
          }
      });

      const results = await Promise.all(requests);
      return results.some(result => result === true);
  } catch (error) {
      throw error;
  }
};

export const suppBonusMalus = async (teamMembers) => {
  try {
      const requests = teamMembers.map(async (member) => {
          const response = await customFetch(`v1.0/bonusMalus/supprimer/${member.bm_note.id}`, {
            method: 'DELETE',
          });
          if (!response.ok) {
              throw new Error('Erreur lors de la requête.');
          }
      });

      await Promise.all(requests);

  } catch (error) {
      console.error(`Une erreur s'est produite lors de la suppression des BM :`, error);
      throw error;
  }
};