<script setup>

import TableThead from './TableThead.vue';
import CircleX from '../../assets/icons/CircleX.vue';
import { ref, watch } from 'vue';
import { defineProps } from 'vue';
import { fetchData, fetchMoyenne } from '../../services/EtudiantNotesService';
import Skeleton from 'primevue/skeleton';


const props = defineProps({
    etudiant: Object
});

const loading = ref(true);
const loaded = ref(false);
const notes = ref(null);
const matieres = ref(null);
const moyenne = ref(null);


async function loadData() {
    await fetchData(matieres, loading, notes, props.etudiant);
    moyenne.value = await fetchMoyenne(props.etudiant);
}

loadData();


</script>

<template>
    <div v-if="matieres && notes && !loading && moyenne != null" class="overflow-x-auto rounded-lg shadow-sm border border-gray-200">
        <table class="w-full text-sm text-left rtl:text-right text-gray-500">
            <TableThead :colonnes="matieres" />
            <tr class="bg-white border-b   hover:bg-gray-50 ">
                <td v-for="matiere in matieres" class="px-6 py-1 font-Roboto"
                    v-bind:class="{ 'font-bold': matiere === 'MOYENNE' }">
                    <span v-if="matiere === 'MOYENNE'">
                        <span v-if="moyenne == 0">
                            <CircleX width=14 class="text-custom-red" />
                        </span>
                        <span v-else>
                            {{ moyenne.toFixed(2) }}
                        </span>

                    </span>
                    <span v-else-if="notes.find(note => note.matiere === matiere)">
                        {{ notes.find(note => note.matiere === matiere).note }}
                    </span>
                    <span v-else>
                        <CircleX width=14 class="text-custom-red" />
                    </span>
                </td>
            </tr>
        </table>
    </div>
    <div v-else>
        <Skeleton width="745px" height="69px"></Skeleton>
    </div>

</template>