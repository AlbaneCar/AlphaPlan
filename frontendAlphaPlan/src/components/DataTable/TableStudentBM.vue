<script setup>
import TableThead from './TableThead.vue';
import Illustration from '../NoDataIllustration/Illustration.vue';
import Label from './Label.vue';
import ButtonBM from '../Buttons/ButtonBM.vue';
import { CircleX } from 'lucide-vue-next';
import { defineEmits } from 'vue';
import { defineProps, ref, watch } from 'vue';


const props = defineProps({
    membres: Array,
    bm: Array
});

const membres = ref(props.membres);
const bm = ref(props.bm);

//Chanegement des refs si changement des props
watch(() => props.membres, (newValue) => {
    membres.value = newValue;
});

watch(() => props.bm, (newValue) => {
    bm.value = newValue;
});

const columns = ["Nom Prénom", "B/M déjà attribués", "Votre B/M"];

const emits = defineEmits(['open']);

let getBonusMalusForMember = (membreId) => {
    const bmTeSS = bm.value.filter(bm => bm.eleve.id === membreId && bm.typeNoteEleve == "TE_BM");
    const bmSsBm = bm.value.filter(bm => bm.eleve.id === membreId && bm.typeNoteEleve == "SS_BM" && bm.evaluateur.id != localStorage.getItem('user_id'));
    
    return [...bmTeSS, ...bmSsBm];
};

let getBonusMalusFromUser = (membreId) => {
    const bonusMalus = bm.value.filter(bm => bm.eleve.id === membreId && bm.evaluateur.id == localStorage.getItem('user_id') && bm.typeNoteEleve == "SS_BM");
    if (bonusMalus[0]) return bonusMalus[0];
    return null;
};

</script>

<template>
    <table class="w-full text-sm text-left rtl:text-right text-gray-500">
        <TableThead :colonnes="columns" />
        <tr v-for="membre in membres" class="border-b hover:bg-gray-50 bg-white">
            <td class="px-6 py-2">{{ membre.nom }} {{ membre.prenom }}</td>
            <td class="px-6 py-2">
                <div class="flex justify-start items-center gap-1">
                    <Label v-for="bm in getBonusMalusForMember(membre.id)" :key="bm.id" :type="bm.note > 0 ? 1 : 4"
                        v-tooltip.top="{
                            value: `Donné par ${bm.evaluateur.nom} ${bm.evaluateur.prenom}\nRaison : ${bm.commentaire}`,
                            pt: {
                                arrow: {
                                    style: {
                                        borderBottomColor: 'var(--danger-color)'
                                    }
                                },
                                text: 'bg-danger font-Roboto text-xs'
                            }
                        }">
                        {{bm.note > 0 ? '+' : '-'}}{{ bm.note > 0 ? bm.note : -bm.note }}
                    </Label>

                    <CircleX v-if="getBonusMalusForMember(membre.id).length == 0" class="text-gray-300" size="16" />

                </div>
            </td>
            <td class="px-6 py-2">
                <div class="flex flex-row justify-end">
                    <ButtonBM :valeur="getBonusMalusFromUser(membre.id)" :studentId="membre.id"
                        @open="(id, value) => $emit('open', id, value)" />
                </div>
            </td>
        </tr>
        <tr v-if="membres.length == 0">
            <td class="px-6 py-4 text-center" colspan=3>
                <div class="flex flex-col justify-center items-center gap-4">
                    <Illustration src="nothing2.svg" width="150" />
                    <div>Aucun élève dans cette équipe !</div>
                </div>

            </td>
        </tr>
        <thead>
            <tr class="bg-gray-100">
                <td class="px-6 py-4" colspan=3></td>
            </tr>
        </thead>
    </table>
</template>