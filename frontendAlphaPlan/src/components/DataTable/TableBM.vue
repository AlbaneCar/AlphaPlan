<script setup>
import TableTheadBM from './TableTheadBM.vue';
import TableRowBM from './TableRowBM.vue';

import { useRouter } from 'vue-router';
import { ref, defineProps, defineEmits } from 'vue';


const router = useRouter();

const props = defineProps({
  advancment: Number,
  teamId: String,
  totalBonus: Number,
  totalMalus: Number,
  teamMembers: Array
});

// Colonnees du tableau
const columns = ["NOM Prénom", "BONUS/MALUS", "DÉCISION"];

let data = ref({ content: [] });
const nobody = ref(null);
const Membres = ref([]);
const loading = ref(true);

const totalBonus = ref(0);
const totalMalus = ref(0);

const emit = defineEmits(['update-totals', 'update-TMBM']);

// MAJ du nombre de BM par membre
const handleTotals = (type, amount) => {
  if (type === 'bonus') {
    totalBonus.value += amount;
    emit('update-totals', 'bonus', amount);
  } else if (type === 'malus') {
    totalMalus.value += amount;
    emit('update-totals', 'malus', amount);
  }
}

const handleBMTM = (memberId, BM) => {
    emit('update-TMBM', memberId, BM);
};


</script>

<template>
    <div class="w-full flex-1">
        <div v-if="!nobody"
            class="overflow-x-auto shadow-md rounded-lg flex flex-col justify-start items-center w-full">
            <table class="w-full text-sm text-left rtl:text-right text-gray-500">

                <!-- En-têtes du tableau -->
                <TableTheadBM :colonnes="columns" />
                <tbody>

                    <!-- Lignes du tableau -->
                    <TableRowBM :advancment="advancment" :teamMembers="teamMembers" @update-totals="handleTotals" @update-TMBM="handleBMTM" />
                </tbody>
                <tfoot class="bg-gray-100">
                    <tr>
                        <td class="text-xl text-gray-700 font-CASlalomBold" colspan="100%">
                            <span class="invisible">Bandeau</span>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </div>
    </div>
</template>