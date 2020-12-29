/*
 *
 * Copyright 2020 Salvatore Giampa'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import jtuple.BoundTuple;
import jtuple.TupleSchema;

public class BoundTupleExample {
    public static void main(String[] args) {
        TupleSchema tupleSchema = TupleSchema.create()
                .addAttribute("NAME", String.class)
                .addAttribute("SURNAME", String.class)
                .addAttribute("AGE", Integer.class)
                .build();

        BoundTuple bilbo = BoundTuple.of(tupleSchema, "Bilbo", "Baggins", 111);
        BoundTuple peregrino = BoundTuple.of(tupleSchema, "Peregrino", "Tuc", 30);

        int ageOfBilbo = bilbo.get("AGE", Integer.class);
        String surnameOfPeregrino = peregrino.get("SURNAME", String.class);

        System.out.println(bilbo);
        System.out.println(peregrino);
        System.out.println("ageOfBilbo=" + ageOfBilbo);
        System.out.println("surnameOfPeregrino=" + surnameOfPeregrino);
    }
}
