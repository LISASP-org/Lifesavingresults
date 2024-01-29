import Image from "next/image";

export default function Home() {
    return (
        <main>
            <h1>DEM2024</h1>
            <h2>Einzel</h2>
            <table className="table-auto w-full">
                <thead>
                <th>Name</th>
                <th>Gliederung</th>
                <th>Geschlecht</th>
                <th>200m Hindernis</th>
                <th>50m Retten</th>
                <th>100m Retten mit Flossen</th>
                <th>100m Lifesaver</th>
                <th>100m k. Rettungsübung</th>
                <th>200m Super-Lifesaver</th>
                </thead>
                <tr className="table-odd">
                    <td>Max Mustermann</td>
                    <td>Musterhausen</td>
                    <td>männlich</td>
                    <td>1:59,13</td>
                    <td>0:28,95</td>
                    <td>0:50,86</td>
                    <td>0:51,56</td>
                    <td>1:08,45</td>
                    <td>2:35,68</td>
                </tr>
            </table>
            <h2>Staffel</h2>
            <table className="table-auto w-full">
                <thead>
                <th>Name</th>
                <th>Gliederung</th>
                <th>Geschlecht</th>
                <th>Line Throw</th>
                <th>4x50m Obstacle Relay</th>
                <th>4x25m Manikin Relay</th>
                <th>4x50m Medley Relay</th>
                <th>4x50m Mixed Pool Lifesaver Relay</th>
                </thead>
                <tr className="table-odd">
                    <td>Musterhausen</td>
                    <td>Musterhausen</td>
                    <td>weiblich</td>
                    <td>0:10,62</td>
                    <td>1:59,67</td>
                    <td>1:40,58</td>
                    <td>1:43,58</td>
                    <td></td>
                </tr>
                <tr className="even">
                    <td>Musterhausen</td>
                    <td>Musterhausen</td>
                    <td>mixed</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>1:58,47</td>
                </tr>
            </table>
        </main>
    );
}
