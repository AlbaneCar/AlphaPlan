<template>
  <form @submit.prevent="submitForm" class="max-w-md mx-auto p-8 bg-white rounded shadow-lg" style="font-family: 'Roboto Regular', sans-serif;">
    <h2 class="text-center mb-4">Ajouter un encadrant</h2>
    <div class="mb-4">
      <input v-model="prenom" type="text" placeholder="Prénom" required class="w-full px-3 py-2 border rounded">
    </div>
    <div class="mb-4">
      <input v-model="nom" type="text" placeholder="Nom" required class="w-full px-3 py-2 border rounded">
    </div>
    <div class="mb-4">
      <select v-model="genre" class="w-full px-3 py-2 border rounded">
        <option value="" disabled selected>Genre</option>
        <option value="HOMME">Masculin</option>
        <option value="FEMME">Féminin</option>
      </select>
    </div>
    <div class="mb-4">
      <select v-model="typeUtilisateur" class="w-full px-3 py-2 border rounded">
        <option value="ENCADRANT">Professeur</option>
      </select>
    </div>
    <div class="text-center">
      <button type="submit" class="px-4 py-2 bg-blue-500 text-white rounded">Ajouter</button>
    </div>
  </form>
</template>

<script setup>
import { addTeacher } from '../services/TeacherService';
import { ref } from 'vue';

const prenom = ref('');
const nom = ref('');
const genre = ref('');
const typeUtilisateur = ref('ENCADRANT');

const submitForm = async () => {
  const payload = {
    prenom: prenom.value,
    nom: nom.value,
    genre: genre.value,
    typeUtilisateur: typeUtilisateur.value
  };
  const success = await addTeacher(payload);
  if (success) {
    alert("Utilisateur ajouté !");
    location.reload();
  }
};
</script>
