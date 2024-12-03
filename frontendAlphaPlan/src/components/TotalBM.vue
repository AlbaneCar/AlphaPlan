<script setup>
import { useRouter } from 'vue-router';
import { ref, defineProps } from 'vue';

const router = useRouter();

const props = defineProps({
  Bvalide: Number,
  Mvalide: Number,
  totalBonus: Number,
  totalMalus: Number,
  advancment: Number
});

</script>

<template>
    <div class="flex-1 relative rounded-lg p-2">
        <span class="block font-CASlalomBold text-black text-xl relative mt-3 ml-3">Récapitulatif des Bonus & Malus</span>
        <div class="absolute top-0 left-0 w-[480px] h-[217px] rounded-xl border border-black"></div>
        <div class="absolute top-[70px] left-[230px] w-[2px] h-[120px] bg-gray-300"></div>
        <div v-if="advancment == 1">
            <span class="absolute top-[82px] left-[88px] flex justify-center items-center font-CASlalomBold text-red-600 text-6xl">0</span>
            <span class="absolute top-[82px] left-[331px] flex justify-center items-center font-CASlalomBold text-blue-500 text-6xl">0</span>
        </div>
        <div v-if="advancment == 3 || advancment == 4 || advancment == 5">
            <span class="absolute top-[82px] left-[91px] flex justify-center items-center font-CASlalomBold text-red-600 text-6xl">{{ Mvalide }}</span>
            <span class="absolute top-[82px] left-[334px] flex justify-center items-center font-CASlalomBold text-blue-500 text-6xl">{{ Bvalide }}</span>
        </div>
        <div v-if="advancment == 2">
            <span class="absolute top-[82px] flex justify-center items-center font-CASlalomBold text-red-600 text-6xl {{ totalMalus < 0 ? left-[83px] : left-[88px] }}">{{ totalMalus == 0 ? '' : '-' }}{{ totalMalus }}</span>
            <span class="absolute top-[82px] flex justify-center items-center font-CASlalomBold text-blue-500 text-6xl {{ totalBonus > 0 ? left-[326px] : left-[331px] }}">{{ totalBonus == 0 ? '' : '+' }}{{ totalBonus }}</span>
        </div>
        <span class="absolute top-[150px] left-[57px] flex justify-center items-center font-CASlalomBold text-red-600 text-4xl">Malus</span>
        <span class="absolute top-[150px] left-[294px] flex justify-center items-center font-CASlalomBold text-blue-500 text-4xl">Bonus</span>
    </div>
</template>