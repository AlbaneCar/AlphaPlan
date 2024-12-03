import {useToast} from 'vue-toastification';
import { customFetch } from '../utils/FetchUtil';

const toast = useToast();



export async function modifierCoefMatiere(nomMatiere, nouveauCoef) {
    try {
      const response = await customFetch(`notes/coef/modify?nomMatiere=${nomMatiere}&nouveauCoef=${nouveauCoef}`, {
          method: 'PUT'
      });
      if (response.ok) {
        toast.success('Coefficient modifié avec succès !', {position : 'top-center'});
      } else {
        toast.error('Erreur lors de la modification du coefficient', {position : 'top-center'});
        throw new Error('Erreur lors de la modification du coefficient');
      }
    } catch (error) {
      toast.error('Erreur lors de la modification du coefficient', {position : 'top-center'});
      throw error;
    }
  }
