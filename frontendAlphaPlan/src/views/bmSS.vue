<script setup>
import Title from '../components/Title.vue'
import Chooser from '../components/Chooser/Chooser.vue'
import DropdownSprints from '../components/DropdownSprints.vue';
import Box from '../components/Box/Box.vue';
import { ref } from 'vue';
import TableStudenBM from '../components/DataTable/TableStudentBM.vue';
import DialogBM from '../components/Dialog/DialogBM.vue';

import { fetchBMEquipe } from '../services/BonusMalusSSService';
import { watch } from 'vue';
import Illustration from '../components/NoDataIllustration/Illustration.vue';
import { fetchEquipes } from '../services/EquipesService';
import { getAllSprints } from '../services/SprintService';

const equipe = ref(['',]);
const visibiliteModal = ref(false);
const selectedStudent = ref(null);
const valeur = ref(0);
const sprint = ref(null);
const user_id = ref(localStorage.getItem("user_id"));
const bm_equipes = ref(null);
const totalBonus = ref(0);
const totalMalus = ref(0);

//Equipes et Sprints
const equipes = ref(null);
const sprints = ref(null);

function changeVisibility() {
    visibiliteModal.value = !visibiliteModal.value;
}

function openModalStudent(id, value) {
    let personne = equipe.value[1].find(item => item.id === id);
    selectedStudent.value = personne;
    valeur.value = value;
    changeVisibility();
}

async function fetchBMEquipeData() {
    bm_equipes.value = await fetchBMEquipe(equipe.value[0], sprint.value[0].id);

    //Enlever les bm tels que bm.note <= 0 : 
    bm_equipes.value = bm_equipes.value.filter(bm => bm.note != 0);

    // Filtrer les bm attribués par l'utilisateur
    let bm_user = bm_equipes.value.filter(bm => bm.evaluateur.id == user_id.value && bm.typeNoteEleve == "SS_BM");

    // Calculer le total des bonus et malus attribués par l'utilisateur
    totalBonus.value = bm_user.filter(bm => bm.note > 0).reduce((acc, bm) => acc + bm.note, 0);
    totalMalus.value = bm_user.filter(bm => bm.note < 0).reduce((acc, bm) => acc + bm.note, 0);

}

watch(() => sprint.value, async (value) => {
    if (equipe.value[0] != '' && sprint.value[0].id != null) {
        fetchBMEquipeData();
    }

});

watch(() => equipe.value, async (value) => {
    if (sprint.value[0].id != null && equipe.value[0] != '') {
        fetchBMEquipeData();
    }
});


// Récupérer les équipes et les sprints :
async function getEquipesData() {
    equipes.value = await fetchEquipes();
}

async function getSprintsData() {
    sprints.value = await getAllSprints();
}

getEquipesData();
getSprintsData();
</script>

<template>
    <Suspense>
        <div v-if="sprint != 0 && equipe[0] != null" class="flex flex-col h-screen w-full p-8 overflow-y-auto gap-4 bg-custom-sideBarBG">
            
            <Title>Gestion des bonus & malus</Title>
            <p class="text-sm text-custom-darkblueAP font-Roboto">
                Vous avez la possibilité de modifier les bonus et malus de chaque étudiant de chaque équipe.<br> Ils
                peuvent avoir une valeur comprise entre -20 et 20. Attention à prendre en compte les bonus malus
                déjà attribués !
            </p>

            <div class="flex flex-wrap justify-between items-center">
                <div>
                    <Chooser v-if="sprints" :sprints="sprints" type="SPRINTS" v-model="sprint" class="items-center" />
                </div>
                <div>
                    <Chooser v-if="equipes" v-model="equipe" :equipes="equipes" type="EQUIPES" class="items-center" />
                </div>
            </div>

            <div class="flex flex-col justify-start items-center h-full w-full gap-6">
                <div
                    class="overflow-x-auto shadow-sm rounded-md flex justify-start items-center w-full border border-gray-200">
                    <TableStudenBM v-if="sprint && equipe && equipe.length != 0 && bm_equipes" :membres="equipe[1]"
                        :bm="bm_equipes" @open="(id, value) => openModalStudent(id, value)" />
                </div>
                <div class="flex justify-end items-center w-full">
                    <div class="flex gap-4">
                        <div
                            class="uppercase font-CASlalomBold text-custom-lightblueAP text-xs flex bg-white rounded-md border border-gray-200 p-2">
                            Bonus donnés : {{ totalBonus }}
                        </div>
                        <div
                            class="uppercase font-CASlalomBold text-custom-red text-xs flex bg-white rounded-md border border-gray-200 p-2">
                            Malus donnés : {{ totalMalus }}
                        </div>
                    </div>
                </div>
            </div>

            <div class="h-0">
                <DialogBM v-if="selectedStudent" :visibilite="visibiliteModal" @update:visibilite="() => changeVisibility()"
                :student="selectedStudent" :value=valeur :sprint="sprint" @newBM="fetchBMEquipeData" />
            </div>

        </div>
        <div v-else class="flex-1 p-6 flex flex-col h-screen overflow-y-auto gap-6 justify-center items-center">
            <div class="flex flex-row items-center">
                <Illustration nameImage="nothing3.svg" width=250 />
                <Title>Les sprints ou les équipes ne sont pas encore définis</Title>
            </div>
        </div>
    </Suspense>

</template>