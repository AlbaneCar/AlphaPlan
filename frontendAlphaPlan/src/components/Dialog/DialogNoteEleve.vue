<script setup>
import Dialog from 'primevue/dialog';
import Button from '../Buttons/Button.vue';
import Carousel from 'primevue/carousel';

import { ref, watch, defineProps } from 'vue';
import { fetchNoteByTypeAndId } from '../../services/NotesService.js';

const NAMES = {
    'PRMA': 'Gestion de projet',
    'TESO': 'Soluation technique',
    'SPCO': 'Conformité du sprint',
    'SUPR': 'Support de la présentation',
    'TEWO': 'Travail en équipe',
    'TEBM': 'Bonus/malus d\'équipe',
    'SSBM': 'Bonus/malus des encadrants',
    'INSP': 'Note personnelle de sprint',
    'SSPR': 'Support de la présentation accordée par les encadrants',
    'OTPR': 'Support de la présentation accordée par les autres équipes',
    'INPR': 'Note individuelle de la présentation',
    'IGSP': 'Note individuelle globale de sprint'
};

const visible = ref(false);
const notes = ref([]);

const props = defineProps({
    type: String,
    eleveId: Number,
    sprintId: Number,
    visibilite: Boolean
});

const emits = defineEmits(['update:visibilite', 'close']);

watch(() => props.visibilite, (newValue) => {
    visible.value = newValue;
});

const closeDialog = () => {
    visible.value = false; // Ferme la boîte de dialogue
    emits('update:visibilite', false);
    emits('close');
};

const loadData = async () => {
    const data = await fetchNoteByTypeAndId(props.eleveId, props.sprintId, props.type);
    notes.value = data;
};

watch(() => props.type, () => {
    loadData();
});

// Fonction permettant d'afficher au maximum une note à deux décimales après la virgule
const displayNote = (note) => {
    // Si la note est un nombre entier, on la retourne telle quelle
    if (Number.isInteger(note)) {
        return note;
    }
    return note.substring(0, 5) === "0.000" ? "0" : note.substring(0, 5);
};

</script>

<template>
    <div>
        <Dialog v-model:visible="visible" modal class="w-1/2" :closable="false"
        :header="'Détails de la note \'' + NAMES[type.toUpperCase()] + '\''">

            <!-- Si aucune note n'est disponible -->
            <div v-if="notes.length === 0" class="flex flex-row justify-center m-5">
                <p>Aucune note pour cette matière</p>
            </div>

            <!-- Si des notes sont disponibles -->
            <Carousel v-else :value="notes" :numVisible="2" :numScroll="1" :showIndicators="false">
                <template #item="note">
                    <div class="flex flex-col border rounded-md shadow-md p-5 m-5 h-fit">
                        <div class="border-b">{{ note.data.evaluateur.prenom + ' ' + note.data.evaluateur.nom }}</div>
                        <div class="flex flex-row h-fit justify-center items-center">
                            <div class="text-xl p-2 justify-center items-center w-fit">{{ displayNote(note.data.note) }}</div>
                            <div class="text-pretty italic p-2 w-full max-h-[30vh] overflow-auto break-word">
                                <p>{{ note.data.commentaire }}</p>
                            </div>
                        </div>
                    </div>
                </template>
            </Carousel>

            <!-- Bouton de fermeture -->
            <div class="flex flex-row justify-center">
                <Button @click="closeDialog" class="bg-custom-lightblueAP text-white px-3 py-2">Fermer</Button>
            </div>

        </Dialog>
    </div>
</template>