<template>
    <Suspense>
        <div class="flex flex-col h-screen w-full p-8 overflow-y-auto bg-custom-sideBarBG gap-4">
            <!-- Titre de la page -->
            <Title>Résumé des notes</Title>
            <!-- Texte explicatif -->
            <p v-if="!userRole.includes('TEAM_MEMBER')" class="text-sm font-Roboto text-custom-darkblueAP">
                Vous trouverez ici un résumé des notes de chaque élève pour un sprint donné.
                <br>
                Sélectionnez un sprint et une équipe pour afficher les notes.
                <br>
                <br>
                Vous pouvez consulter le détail d'une des notes en cliquant dessus.
                <br>
                De plus, il est possible d'exporter les notes de l'équipe en cliquant sur le bouton "Exporter les
                notes".
                <br>
                Enfin, vous pouvez voir le détail du calcul des notes en cliquant sur le bouton "Information sur le
                calcul".
            </p>
            <p v-else class="text-sm font-Roboto text-custom-darkblueAP">
                Vous trouverez ici un résumé de vos notes pour un sprint donné ainsi que celles de vos camarades.
                <br>
                Sélectionnez un sprint pour afficher vos notes.
                <br>
                <br>
                Vous pouvez consulter le détail d'une de vos notes en cliquant dessus.
                <br>
                Enfin, vous pouvez voir le détail du calcul des notes en cliquant sur le bouton "Information sur le
                calcul".
            </p>

            <div class="flex flex-row justify-between">
                <!-- Sélecteur de Sprint (Chooser) -->
                <Chooser v-if="sprints" :sprints="sprints" type="SPRINTS" v-model="sprint" class="items-center" />
                <!-- Sélecteur d'équipe (Chooser) -->
                <Chooser v-if="equipes && !userRole.includes('TEAM_MEMBER')" v-model="equipe" :equipes="equipes"
                    :sprints="sprints" type="EQUIPES" class="items-center" />
            </div>
            <Suspense>
                <!-- Tableau des notes de l'équipe -->
                <TabNotes :equipe="equipe" :sprint="sprint" />
            </Suspense>
        </div>
    </Suspense>
</template>

<script setup>
import { ref } from 'vue';
import Chooser from '../components/Chooser/Chooser.vue';
import Title from '../components/Title.vue';
import TabNotes from '../components/Notes/TabNotes.vue';
import { fetchEquipes } from '../services/EquipesService';
import { getAllSprints } from '../services/SprintService';

// Variables
const equipe = ref([]);
const sprint = ref([]);
const userRole = buildRoleString(JSON.parse(localStorage.getItem('roles')));

const equipes = ref(null);
const sprints = ref(null);

// Fonction pour construire une chaîne de caractères des rôles de l'utilisateur
function buildRoleString(roles) {
    let roleString = '';
    roles.forEach(role => {
        roleString += role + '/';
    });
    return roleString;
}

async function getEquipesData() {
    equipes.value = await fetchEquipes();
}

async function getSprintsDate() {
    sprints.value = await getAllSprints();
}

getEquipesData();
getSprintsDate();

</script>