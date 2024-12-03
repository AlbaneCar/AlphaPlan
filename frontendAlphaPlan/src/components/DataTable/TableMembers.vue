<script setup>

import { defineProps } from 'vue';
import TableTheadBM from './TableTheadBM.vue';
import Label from './Label.vue';

const props = defineProps({
    equipe: Array,
});

</script>

<template>
    <div class="w-full flex-1">
        <div v-if="!nobody"
            class="overflow-x-auto shadow-sm rounded-lg flex flex-col justify-start items-center w-full border border-gray-200">
            <table class="w-full text-sm text-left rtl:text-right text-gray-500">
                <thead class="text-xs text-gray-700 bg-gray-100 font-CASlalomBold">
                    <tr>
                        <th class="px-6 py-3 uppercase">Nom</th>
                        <th class="px-6 py-3 uppercase">Prénom</th>
                        <th class="px-6 py-3 uppercase">Genre</th>
                        <th class="px-6 py-3 uppercase text-end">Cursus</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="membre in equipe.membres" :key="membre.id" class="border-b border-gray-200 hover:bg-gray-50">
                        <td class="px-6 py-2 uppercase">{{ membre.nom }}</td>
                        <td class="px-6 py-2">{{ membre.prenom }}</td>
                        <td class="px-6 py-2">
                            <!--Genre-->
                            <Label v-if="membre.genre == 'HOMME'" :type="1" v-tooltip.top="{
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
                        <td class="px-6 py-2 flex justify-end">
                            <!--Cursus-->
                            <Label v-if="membre.typeUtilisateur == 'BACHELOR'" :type="3">Bachelor</Label>
                            <Label v-else :type="2">E3e</Label>
                        </td>
                    </tr>
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