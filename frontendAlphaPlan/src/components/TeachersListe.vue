<template>
  <div class="text-center mx-6 p-8" style="font-family: 'Roboto Regular', sans-serif;">
    <h1 class="font-bold text-2xl mb-6">Liste des encadrants :</h1>
    <table class="w-full border-collapse">
      <thead>
        <tr>
          <th class="px-4 py-2 border">Prénom</th>
          <th class="px-4 py-2 border">Nom</th>
          <th class="px-4 py-2 border">Genre</th>
          <th class="px-4 py-2 border">Type</th>
          <th class="px-4 py-2 border"></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(user, index) in users" :key="index" class="bg-white border">
          <td class="px-4 py-2 border">{{ user.prenom }}</td>
          <td class="px-4 py-2 border">{{ user.nom }}</td>
          <td class="px-4 py-2 border">{{ user.genre }}</td>
          <td class="px-4 py-2 border">{{ user.typeUtilisateur }}</td>
          <td class="px-4 py-2 border">
            <span @click="confirmDelete(user.id)" class="mr-4 cursor-pointer">❌</span>
            <span class="cursor-pointer">⋮</span>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { fetchUsers, deleteUser } from '../services/TeacherService';

const users = ref([]);

onMounted(async () => {
  users.value = await fetchUsers();
});

const confirmDelete = async (userId) => {
  if (confirm(`Êtes-vous sûr de vouloir supprimer cet utilisateur ?`)) {
    const deleted = await deleteUser(userId);
    if (deleted) {
      alert("Utilisateur supprimé !");
      location.reload();
    }
  }
}
</script>

<style scoped>
table {
  line-height: 150%;
}
h1 {
  line-height: 120%;
}
</style>
