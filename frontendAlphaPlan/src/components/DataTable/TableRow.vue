<script setup>
defineProps({
    etudiant: Object
})

import { useRouter } from 'vue-router';
import routeNames from '../../name';
import Label from './Label.vue';
import CircleX from '../../assets/icons/CircleX.vue';
import Plus from '../../assets/icons/Plus.vue';

const router = useRouter();
const emit = defineEmits(['click:student']);


</script>

<template>
    <tr class="bg-white border-b   hover:bg-gray-50 ">
        <td class="px-6 py-2 uppercase">
            {{ etudiant.nom }}
        </td>
        <td class="px-6 py-2">
            {{ etudiant.prenom }}
        </td>
        <td class="px-6 py-2">
            <Label v-if="etudiant.genre == 'HOMME'" :type="1" v-tooltip.top="{
                        value: `Homme`,
                        showDelay: 500,
                        pt: {
                            text: 'text-xs px-2 py-1'
                        }
                    }">H</Label>
            <Label v-else :type="0" v-tooltip.top="{
                        value: `Femme`,
                        showDelay: 500,
                        pt: {
                            text: 'text-xs px-2 py-1'
                        }
                    }">
                F
            </Label>
        </td>
        <td class="px-6 py-2">
            <Label v-if="etudiant.typeUtilisateur == 'BACHELOR'" :type="3">Bachelor</Label>
            <Label v-else :type="2">E3e</Label>
        </td>
        <td class="px-6 py-2">
            <span v-if="etudiant.equipe" class="italic">{{ etudiant.equipe.nom }}</span>
            <span v-else>
                <CircleX :width="14" class="text-custom-red" />
            </span>
        </td>
        <td class="px-6 py-2">
            <div class="flex justify-end items-center h-full">
                <Plus :width="14" @click="emit('click:student', etudiant)" class="cursor-pointer hover:text-custom-lightblueAP transition-colors" />
            </div>
            
        </td>
    </tr>
</template>