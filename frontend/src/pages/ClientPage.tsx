import React, {useEffect, useState} from "react";
import {useKeycloak} from "@react-keycloak/web";
import {useNavigate} from "react-router-dom";

type CurrentClient = {
    username: string;
    amount: number;
};

type Tour = {
    id: number;
    name: string;
    price: string;
    count: number;
};

export const ClientPage = () => {
    const {keycloak} = useKeycloak();
    const navigate = useNavigate();
    const [currentClient, setCurrentClient] = useState<CurrentClient>();
    const [tours, setTours] = useState<Tour[]>();

    const updateCurrentClient = () => {
        fetch("http://localhost:8080/servlet_war_exploded/current-client", {
            method: "GET",
            headers: {Authorization: "Bearer " + keycloak.token},
        })
            .then((r) => r.json())
            .then(client => {
                setCurrentClient(client);
            })
            .catch(console.error);
    }

    const updateTours = () => {
        fetch("http://localhost:8080/servlet_war_exploded/tour")
            .then((r) => r.json())
            .then(setTours)
            .catch(console.error);
    }

    const orderTour = (id: number) => {
        fetch(
            "http://localhost:8080/servlet_war_exploded/order",
            {
                method: "PUT",
                headers: {
                    Authorization: "Bearer " + keycloak.token,
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({id: id}),
            }
        )
            .then(() => updateScreen())
            .catch(console.error);
    }

    const updateScreen = () => {
        updateCurrentClient();
        updateTours()
    };

    useEffect(() => {
        setTimeout(() => {
            if (!keycloak.authenticated) {
                navigate("/");
                return;
            }
            updateScreen();
        }, 300);
    }, []);

    if (!currentClient || !tours) {
        return <h1>Loading...</h1>;
    }
    return (
        <>
            <h1>
                {currentClient.username}: {currentClient.amount}$
            </h1>
            <button onClick={() => keycloak.logout()}>Logout</button>
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Count exist</th>
                    <th>Buy</th>
                </tr>
                </thead>
                <tbody>
                {tours.map((d) => (
                    <tr key={"tr_" + d.id}>
                        <td>{d.name}</td>
                        <td>{d.price}</td>
                        <td>{d.count}</td>
                        <td>
                            <button onClick={() => orderTour(d.id)}>
                                Order
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </>
    );
};
