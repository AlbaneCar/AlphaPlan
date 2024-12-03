<template>
  <div class="p-4 min-w-60 flex flex-col gap-4">
    <div>
      <label for="matiere" class="block text-sm font-medium text-gray-700">Matière :</label>
      <select v-model="selectedMatiere" @change="updateNouveauCoef" id="matiere"
        class="mt-1 block w-full py-2 px-3 border border-gray-300 rounded-md focus:outline-none">
        <option v-for="matiere in matieresAvecCoefficients" :key="matiere.nom" :value="matiere">
          {{ matiere.nom }}
        </option>
      </select>
    </div>

    <div>
      <label for="coef" class="block text-sm font-medium text-gray-700">Coefficient :</label>
      <input type="number" v-model="nouveauCoef" id="coef" min="0" step="0.5"
        class="mt-1 block w-full py-2 px-3 border border-gray-300 rounded-md focus:outline-none">
    </div>

    <button @click="changerCoef"
      class="bg-custom-lightblueAP font-CASlalomBold text-white  py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-opacity-50 hover:bg-custom-darkblueAP duration-300">Changer</button>
  </div>
</template>

<script>
import { ref } from 'vue';
import { getMatieres, getMatieresWithCoefficients } from '../services/MatiereService';
import { modifierCoefMatiere } from '../services/CoefService';
import { useToast } from 'vue-toastification';

const toast = useToast();

export default {
  data() {
    return {
      matieresAvecCoefficients: [],
      selectedMatiere: null,
      nouveauCoef: null,
    };
  },
  async mounted() {
    try {
      this.matieresAvecCoefficients = await getMatieresWithCoefficients();
    } catch (error) {
      console.error('Erreur lors de la récupération des matières avec coefficients :', error);
    }
  },
  methods: {
    async changerCoef() {
      if (this.nouveauCoef <= 0) {
        toast.error('Le coefficient ne peut pas être négatif !', {position : 'top-center'});
        return;
      }

      try {
        await modifierCoefMatiere(this.selectedMatiere.nom, this.nouveauCoef);
      } catch (error) {
        console.error('Erreur lors de la modification du coefficient :', error);
      }
    },
    updateNouveauCoef() {
      this.nouveauCoef = this.selectedMatiere ? this.selectedMatiere.coefficient : null;
    }
  }
};
</script>
