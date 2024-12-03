<script setup>
import TableThead from './TableThead.vue';
import TableRow from './TableRow.vue';
import Title from '../Title.vue';
import Illustration from '../NoDataIllustration/Illustration.vue';
import Button from '../Buttons/Button.vue';
import Dialog from 'primevue/dialog';
import TableNote from './TableNote.vue';
import DialogImport from '../Dialog/DialogImport.vue';

import { fetchData } from '../../services/EtudiantsViewService';

import { ref } from 'vue';
import { useRouter } from 'vue-router';
import routeNames from '../../name';
import Loader from '../Loader/Loader.vue';
import ProgressSpinner from 'primevue/progressspinner';
import { CircleArrowRight, CircleArrowLeft } from 'lucide-vue-next';

const router = useRouter();

const emit = defineEmits(['nobody']);


const columns = ["Nom", "Prénom", "Genre", "Cursus", "Équipe", ""];

let data = ref({ content: [] });
const nobody = ref(null);
const loading = ref(true);
const loaded = ref(false);
const visible = ref(false);
const etudiant = ref(null);
const visibilite = ref(false);
const loadingNotes = ref(false);

function changeVisibility() {
    visibilite.value = !visibilite.value;
}


async function loadData(pageNumber = 0) {
    await fetchData(nobody, loading, loaded, data, pageNumber);
    emit('nobody', nobody.value);
}

function loadNotesEtudiant(etudiantData) {
    etudiant.value = etudiantData;
    visible.value = true;
}

loadData();

</script>

<template>
    <div v-if="!loading" class="w-full flex-1 mt-8">
        <div v-if="!nobody"
            class="overflow-x-auto shadow-sm rounded-md flex flex-col justify-start items-center w-full border border-gray-200">
            <table class="w-full text-sm text-left rtl:text-right text-gray-500">
                <TableThead :colonnes="columns" />
                <tbody v-if="data.content && data.content.length">
                    <TableRow v-for="etudiant in data.content" :key="etudiant.id" :etudiant="etudiant"
                        @click:student="(etudiantData) => loadNotesEtudiant(etudiantData)" />
                </tbody>
                <thead class="text-xs text-gray-700 uppercase border-t border-gray-200 bg-gray-100 font-CASlalomBold">
                    <tr v-if="data && data.pageable">
                        <th class="px-6 py-3">Page {{ data.pageable.pageNumber + 1 }} sur {{ data.totalPages }}</th>
                        <th class="px-6 py-3 font-CASlalomRegular">
                            {{ data.totalElements }} étudiant<span v-if="data.totalElements != 1">s</span></th>
                        <th class="px-6 py-3"> </th>
                        <th class="px-6 py-3"> </th>
                        <th class="px-6 py-3"> </th>
                        <th class="px-6 py-3 text-right">
                            <div class="flex justify-end items-center gap-8">
                                <h1 v-if="data.pageable.pageNumber != 0" class="cursor-pointer select-none"
                                    @click="() => loadData(pageNumber = data.pageable.pageNumber - 1)">
                                        <CircleArrowLeft class="w-4 text-custom-darkblueAP hover:text-custom-lightblueAP transition-colors" />
                                </h1>
                                <h1 v-if="data.pageable.pageNumber + 1 != data.totalPages" class="cursor-pointer select-none"
                                    @click="() => loadData(pageNumber = data.pageable.pageNumber + 1)">
                                        <CircleArrowRight class="w-4 text-custom-darkblueAP hover:text-custom-lightblueAP transition-colors" />
                                </h1>
                            </div>
                        </th>
                    </tr>
                </thead>
            </table>
        </div>
        <div v-else class="w-full h-full flex flex-col justify-start items-center">
            <div class="flex flex-row w-full h-full justify-center items-center gap-6">
                <Illustration nameImage="nothing.svg" width="300" />
                <div class="flex flex-col justify-center items-center gap-3">
                    <Title>Aucun étudiant encore enregistré !</Title>
                    <Button icon="import" classes="px-4 py-2" @click="() => changeVisibility()">Importer une liste d'étudiants</Button>
                </div>

            </div>
        </div>
        <div class="card flex justify-content-center">
            <Dialog v-if="etudiant" v-model:visible="visible" modal
                :header="`Notes de ${etudiant.prenom} ${etudiant.nom}`" class="min-w-96">
                <TableNote :etudiant="etudiant"/>
            </Dialog>
        </div>
        <DialogImport :visibilite="visibilite" @update:visibilite="() => changeVisibility()" />
    </div>
    <div v-else>

    </div>

</template>