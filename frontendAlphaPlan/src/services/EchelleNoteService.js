import { useToast } from 'vue-toastification';
import {customFetch} from "../utils/FetchUtil";

const toast = useToast();

export async function fetchEchellesNote() {
    try {
        const response = await customFetch('echelleNotes');
        console.log("response", response);
        if (!response.ok) {
            throw new Error('Erreur lors de la récupération des échelles de notes');
        }
        return await response.json();
    
    } catch (error) {
        console.error('Erreur lors de la récupération des échelles de notes:', error);
        throw error;
    }
}


export async function confirmDelete(id) {
    try {
      const response = await customFetch(`echelleNotes/supprimer/${id}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
        throw new Error('Échec de la suppression de l\'échelle de note');
      }
    } catch (error) {
      console.error('Erreur lors de la suppression de l\'échelle de note:', error);  }
  };
  
const getRandomColor = () => {
    const colors = ['#FF5733', '#33FF57', '#3357FF', '#FF33C7']; // Liste de couleurs prédéfinies
    return colors[Math.floor(Math.random() * colors.length)];
};

export async function fetchUsedTypesEchelle() {
  try {
      const response = await customFetch('echelleNotes/useTypes',{
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) {
          throw new Error('Erreur lors de la récupération des types utilisés');
      }
      return await response.json();
  } catch (error) {
      console.error('Erreur lors de la récupération des types utilisés:', error);
      throw error;
  }
}

export async function fetchTypesEchelle() {
  try {
    const response = await customFetch('echelleNotes/types');
    if (!response.ok) {
      throw new Error(`Erreur lors de la récupération des types d'échelle`);
    }
    return await response.json();
  } catch (error) {
    console.error(`Erreur lors de la récupération des types d'échelle: `, error);
    throw error;
  }
}

export async function submitForm(form) {
  try {
    const body = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(form)
    };
    const response = await customFetch('echelleNotes/ajouter', body);
    if (!response.ok) {
      throw new Error('Erreur lors de l\'envoi du formulaire');
    } else {
      toast.success("L'échelle de notes a bien été créée !", {
          position: "top-center",
      });
    }
    
    return true; 
  } catch (error) {
    console.error('Erreur lors de la soumission du formulaire:', error);
    throw error;
  }
}


