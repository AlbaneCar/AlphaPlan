const baseUrl = import.meta.env.VITE_IP_BACKEND;
import { customFetch } from '../utils/FetchUtil';


export async function fetchOrdrePassage(sprint_id, equipe_id) {

    try {
      const response = await customFetch(`v1.0/ordrePassage/${sprint_id}/${equipe_id}`);
      if (response.status === 400) {
        return response.json();
      }
      else if (response.ok) {
        return await response.json();
      }
    } catch (error) {
      throw new Error("Erreur lors de la récupération de l'ordre de passage de l'équipe");
    }
  }

  export async function CreateUpdateOrdrePassage(sprint_id, equipe_id, auteurId, ordres) {

    const url = new URL(baseUrl + `v1.0/ordrePassage/${sprint_id}/${equipe_id}`);
    console.log(url.searchParams);
    url.searchParams.append('auteurId', auteurId);
    console.log(ordres);
    ordres.forEach(ordre => url.searchParams.append('ordre', ordre));
    console.log(url);
    try {
        const response = await customFetch(url.toString(), {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
        });
    
        if (!response.ok) {
          throw new Error(`Erreur lors de l'ajout de l'ordre de passage. error status: ${response.status}`);
        }
        else {
         return await response.text();
        }
      } catch(error) {
        console.error('Error:', error);
        throw new Error("Erreur lors de l'ajout de l'ordre de passage");
      }
    }
  