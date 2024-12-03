<template>
    <form class="w-full h-full font-Roboto flex flex-col border px-8 py-6 rounded-md text-center bg-white border-gray-200 shadow-sm gap-10">
       <h2 class="font-CASlalomBold text-2xl my-auto">Générez vos équipes :</h2>
         <div class="mx-auto my-2 text-sm flex">
           <div class="text-center">
             <label>Nombre d'équipes</label>
             <InputNumber :percentageMode="false" v-model="teamsAmount"/>
           </div>
         </div>
         <div class="my-auto">
            <Button @click.prevent="submitForm" classes="p-2">Générer</Button>
         </div>
    </form>
   </template>
   
<script setup>
    import { ref } from 'vue';
    import InputNumber from './InputNumber.vue';
    import Button from './Buttons/Button.vue';
    import { creerEquipes } from '../services/CreerEquipesService.js'

    const teamsAmount = ref(0);

    const emit = defineEmits(['update-teams-amount']);

    const submitForm = async () => {
        try {
          emit('update-teams-amount', teamsAmount.value);
          const response = await creerEquipes(teamsAmount.value);
          emit('teams-generated', response);
        } catch (error) {
            console.error('Erreur lors de la soumission du formulaire:', error);
        }
    };
</script>
   