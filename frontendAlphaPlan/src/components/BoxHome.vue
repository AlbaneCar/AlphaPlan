<template>
    <div class="flex flex-col gap-2 w-full">

        <template v-for="index in (avancee)" :key="index">
            <div class="w-full h-16 bg-[#71BCEE] rounded-md flex justify-center items-center shadow-md gap-2">
                <CircleCheckBig class="text-white" />
                <div class="text-white font-CASlalomBold">
                    {{ etapes[index - 1] }}
                </div>
            </div>
            <div v-if="index != etapes.length" class="flex justify-center items-center">
                <ArrowBigDownDash />
            </div>
        </template>

        <template v-if="avancee != etapes.length">
            <router-link :to="link[avancee]">
                <div
                    class="w-full h-16 bg-custom-lightblueAP rounded-md flex justify-center items-center shadow-md gap-2 cursor-pointer">
                    <div class="text-white font-CASlalomBold">
                        {{ etapes[avancee] }}
                    </div>
                </div>

                <div v-if="avancee + 1 != etapes.length" class="flex justify-center items-center">
                    <ArrowBigDownDash />
                </div>
            </router-link>
        </template>


        <template v-if="avancee + 1 < etapes.length" v-for="index in (etapes.length - avancee - 1)">
            <div
                class="w-full h-16 bg-gray-400 rounded-md flex justify-center items-center shadow-md gap-2 cursor-not-allowed">
                <div class="text-custom-grey font-CASlalomBold">
                    {{ etapes[avancee + index] }}
                </div>
            </div>
            <div v-if="avancee + index + 1 != etapes.length" class="flex justify-center items-center">
                <ArrowBigDownDash />
            </div>
        </template>
    </div>
</template>

<script setup>
import { ArrowBigDownDash, CircleCheckBig } from 'lucide-vue-next';
import { defineProps } from 'vue';
import routeNames from '../name'


const props = defineProps({
    avancee: Number
});

const etapes = ["Importer vos étudiants", "Générer les équipes de travail", "Paramétrer votre projet"];

const link = [routeNames.ETUDIANTS, routeNames.CREATETEAM, routeNames.SPRINT];
</script>
