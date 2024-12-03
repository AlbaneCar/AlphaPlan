<script setup>
import Title from '../components/Title.vue'
import Table from '../components/DataTable/Table.vue'
import Button from '../components/Buttons/Button.vue'
import DialogImport from '../components/Dialog/DialogImport.vue'
import DialogCoefs from '../components/Dialog/DialogCoefs.vue'
import { onMounted, ref } from 'vue'
import { exportToCSV } from '../services/EtudiantNotesService'

const nobody = ref(null);
const visibleImport = ref(false);
const visibleCoefs = ref(false);

function changeVisibility(toChange) {
    if (toChange == 'import')
        visibleImport.value = !visibleImport.value;
    else if (toChange == 'coefs')
        visibleCoefs.value = !visibleCoefs.value;
}

</script>

<template>
    <div class="flex flex-col h-screen w-full p-8 overflow-y-auto bg-custom-sideBarBG justify-start gap-4">

        <!-- Titre et description de la page -->
        <div class="flex justify-between gap-8">
            <div class="flex flex-col gap-4">
                <Title>Etudiants</Title>
                <p v-if="nobody != null" class="text-sm text-custom-darkblueAP font-Roboto">Retrouvez les informations
                    sur
                    les étudiants inscrits au projet. <br> Consultez facilement les notes antérieures d'un étudiant en
                    appuyant sur +. <br><br> Vous pouvez aussi réaliser un nouvel import d'étudiants, exporter la liste
                    actuelle des étudiants ou bien modifier les coefficients des différentes matières.</p>
            </div>

            <!-- Boutons d'actions -->
            <div v-if="nobody != null & !nobody" class="flex flex-col justify-end items-center gap-6">
                <div class="flex justify-center items-center gap-3">
                    <Button icon='edit' classes="px-4 py-2"
                        @click="() => changeVisibility('coefs')">Coefficients</Button>
                    <Button icon='import' classes="px-4 py-2"
                        @click="() => changeVisibility('import')">Importer</Button>
                    <Button icon='export' classes="px-4 py-2" @click="exportToCSV()">Exporter</Button>
                </div>
            </div>
        </div>



        <Table @nobody="(valeur) => nobody = valeur" />

    </div>
    <div class="h-0">
        <DialogImport :visibilite="visibleImport" @update:visibilite="() => changeVisibility('import')" />
        <DialogCoefs :visibilite="visibleCoefs" @update:visibilite="() => changeVisibility('coefs')" />
    </div>
</template>