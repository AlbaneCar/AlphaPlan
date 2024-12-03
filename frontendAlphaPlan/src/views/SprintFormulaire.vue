<script setup>
import Formulaire from '../components/Formulaire.vue';
import Title from '../components/Title.vue';
import DialogSprint from '../components/Dialog/DialogSprint.vue';
import Button from '../components/Buttons/Button.vue'
import Illustration from '../components/NoDataIllustration/Illustration.vue';

import { ref, watch } from 'vue';
import { fetchSprint } from '../services/SprintService.js';
import AccordionSprint from '../components/Accordion/AccordionSprint.vue';


const visibleSprint = ref(false);
const sprints = ref([]);
const nbSprint = ref(0);
const formSubmitSuccess = ref(false);

// On récupère les sprints
const loadSprints = async () => {
    const response = await fetchSprint();
    sprints.value = response;
};

loadSprints();

const closeDialog = (valeur) => {
    visibleSprint.value = false;
    nbSprint.value = valeur;
};

// Fonction permettant de mettre à jour le parent en fonction de l'enfant
const handleFormSubmitted = (newValue) => {
    formSubmitSuccess.value = newValue;
}

// Watchers
watch(formSubmitSuccess, () => {
    loadSprints();
});

</script>

<template>
    <Suspense>
        <div class="p-8 flex flex-col h-screen w-full gap-4 bg-custom-sideBarBG">
            <Title>Gestion des sprints</Title>
            <AccordionSprint v-if="sprints.length" v-for="sprint in sprints" :key="sprint" :sprints="sprints"
                :index="sprints.indexOf(sprint)" />

            <div v-else class="h-full flex flex-col justify-center">
                <Formulaire v-if="!visibleSprint && nbSprint != 0" :nombre="nbSprint"
                    @update:formSubmitSuccess="handleFormSubmitted" />
                <div v-else class="w-full h-full flex flex-col justify-start items-center">
                    <div class="flex flex-row justify-center items-center gap-3 w-full h-full">
                        <Illustration nameImage="nothing.svg" :width="300" />
                        <div class="flex flex-col justify-center items-center gap-3">
                            <Title>Aucun sprint n'a été créé !</Title>
                            <Button icon='edit' classes="px-3 py-2" @click="() => visibleSprint = true">Créer des
                                sprints</Button>
                        </div>
                    </div>
                    <DialogSprint :visibilite="visibleSprint"
                        @update:visibilite="value => { if (value && value.value !== undefined) visibleSprint.value = value.value; }"
                        @close="(valeur) => closeDialog(valeur)" />
                </div>
            </div>
        </div>
    </Suspense>
</template>