<template>

    <div class="text-center">
      <EtudiantsListe v-if="afficherEtudiants" class="mb-4" />
      <TeachersListe v-else class="mb-4" />
      <button @click="toggleAffichage" class="text-white bg-gradient-to-r from-cyan-500 to-blue-500 hover:bg-gradient-to-bl focus:ring-4 focus:outline-none focus:ring-cyan-300 dark:focus:ring-cyan-800 font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2" style="font-family: 'Roboto Regular', sans-serif;">
        {{ afficherEtudiants ? 'Afficher les Professeurs' : 'Afficher les Étudiants' }}
      </button>
      <FormAjout />
    </div>
  </template>

  <script setup>
  import { ref } from 'vue';
  import { RouterView } from 'vue-router';

  import EtudiantsListe from '../components/EtudiantsListe.vue'
  import TeachersListe from '../components/TeachersListe.vue'
  import FormAjout from '../components/formAjoutEtudiant.vue'

  const afficherEtudiants = ref(true);

  const toggleAffichage = () => {
    afficherEtudiants.value = !afficherEtudiants.value;
  }
  </script>
