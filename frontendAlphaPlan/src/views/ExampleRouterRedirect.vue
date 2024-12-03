<script setup>
import routeNames from '../name';
import { useRouter } from 'vue-router';
const router = useRouter()

function goToAbout() {
  router.push({ name: routeNames.ABOUT })
}

function goToSprint() {
  router.push({ name: routeNames.SPRINT })
}
function goToListeEquipes() {
  router.push({ name: routeNames.LISTE_EQUIPES })
}

function goToListUtilisateurs() {
  router.push({ name: routeNames.LISTE_UTILISATEURS })
}
</script>

<template>
  <h2>HomeView</h2>
  <button @click="goToAbout">Go to About with push</button>
  <button @click="goToSprint">Go to Sprint with push</button>
  <button @click="goToListeEquipes">Go to ListeEquipes</button>
  <button @click="goToListUtilisateurs">Go to ListeUtilisateurs</button>
</template>