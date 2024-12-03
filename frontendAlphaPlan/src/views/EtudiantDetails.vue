<script setup>
import Title from '../components/Title.vue'
import Box from '../components/Box/Box.vue'


import { ref } from 'vue'
import { useRoute } from 'vue-router'


const route = useRoute();
const userId = route.params.etudiantId;
const loading = ref(false);
const etudiant = ref(null);
const visible = ref(false);


async function fetchData() {
    try {
        const response = await fetch(import.meta.env.VITE_IP_BACKEND + `users/${userId}`);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        etudiant.value = await response.json();
        console.log("Elements trouvés : ", etudiant.value);
    } catch (err) {
        console.error(err);
    } finally {
        loading.value = false;
    }
}

fetchData();
</script>

<template>
    <div v-if="etudiant">
            <div class="flex-1 p-6 flex flex-col gap-6">
                <Title :title="`${etudiant.nom} ${etudiant.prenom} - ${$route.params.etudiantId}`"></Title>

                <div class="flex items-center justify-center w-full gap-6">
                    <Box class="flex-1 flex-col justify-start items-start h-full p-3">
                        <h1 class="text-lg font-CASlalomBold">À Propos :</h1>
                        <div class="flex justify-start items-center gap-6">
                            <div>Nom : {{ etudiant.nom }}</div>
                            <div>Prénom : {{ etudiant.prenom }}</div>
                            <div>Email : {{ etudiant.email }}</div>
                        </div>
                    </Box>
                    <Box>Informations sur l'équipe de l'utilisateur</Box>
                </div>
                <div class="flex items-center justify-center w-full gap-6">
                    <Box>Intérieur de la box</Box>
                    <Box class="flex-1">Intérieur de la box</Box>
                </div>
            </div>
        </div>
</template>